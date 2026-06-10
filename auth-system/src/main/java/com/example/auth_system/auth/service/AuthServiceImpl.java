package com.example.auth_system.auth.service;


import com.example.auth_system.auth.dto.request.*;
import com.example.auth_system.auth.dto.response.*;
import com.example.auth_system.auth.entity.*;
import com.example.auth_system.auth.repository.*;
import com.example.auth_system.auth.security.JwtTokenProvider;
import com.example.auth_system.common.exception.AuthException;
import com.example.auth_system.common.exception.InvalidCredentialsException;
import com.example.auth_system.common.exception.InvalidTokenException;
import com.example.auth_system.common.exception.OtpValidationException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.exception.UserAlreadyExistsException;
import com.example.auth_system.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.ROLE_USER)
                .enabled(false)
                .emailVerified(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Send OTP for email verification
        otpService.sendOtp(user.getEmail(), OtpType.REGISTRATION);

        log.info("User registered successfully with id: {}", user.getId());

        return AuthResponse.builder()
                .success(true)
                .message("Registration successful. Please verify your email with OTP")
                .userId(user.getId().toString())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Check if email is verified
        if (!user.isEmailVerified()) {
            throw new AuthException("Please verify your email before logging in");
        }

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new AuthException("Account is disabled. Please contact support");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user);

        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User logged in successfully: {}", user.getEmail());

        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(jwtTokenProvider.generateRefreshToken(user))
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .user(LoginResponse.UserInfo.builder()
                        .id(user.getId().toString())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole().name())
                        .emailVerified(user.isEmailVerified())
                        .build())
                .build();
    }

    @Override
    public void logout(String token) {
        log.info("Logging out user");
        
        // Remove token from cache/blacklist (if implemented)
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtTokenProvider.invalidateToken(token);
        }
        
        log.info("User logged out successfully");
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Processing forgot password for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Generate reset token
        String resetToken = jwtTokenProvider.generatePasswordResetToken(user);
        
        // Save reset token to database
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(resetToken)
                .user(user)
                .used(false)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        
        passwordResetTokenRepository.save(passwordResetToken);

        // Send email with reset link
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        log.info("Password reset token sent to: {}", request.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Resetting password with token");

        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("Passwords do not match");
        }

        // Find and validate reset token
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByTokenAndUsedFalseAndExpiresAtAfter(request.getToken(), LocalDateTime.now())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        // Get user
        User user = resetToken.getUser();

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        // Invalidate all active OTPs for this user
        otpTokenRepository.invalidateAllActiveOtps(user.getEmail(), OtpType.PASSWORD_RESET);

        // Send confirmation email
        emailService.sendPasswordResetConfirmationEmail(user.getEmail());

        log.info("Password reset successfully for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void sendOtp(SendOtpRequest request) {
        log.info("Sending OTP to: {} for type: {}", request.getEmail(), request.getType());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Rate limiting check (optional)
        long otpCount = otpTokenRepository.countOtpsByEmailSince(
                request.getEmail(), 
                LocalDateTime.now().minusHours(1)
        );
        
        if (otpCount >= 5) {
            throw new AuthException("Too many OTP requests. Please try again later");
        }

        otpService.sendOtp(user.getEmail(), request.getType());

        log.info("OTP sent successfully to: {}", request.getEmail());
    }

    @Override
    @Transactional
    public void verifyOtp(VerifyOtpRequest request) {
        log.info("Verifying OTP for: {} type: {}", request.getEmail(), request.getType());

        // Find valid OTP
        OtpToken otpToken = otpTokenRepository
                .findByEmailAndOtpAndTypeAndUsedFalseAndExpiresAtAfter(
                        request.getEmail(),
                        request.getOtp(),
                        request.getType(),
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new OtpValidationException("Invalid or expired OTP"));

        // Mark OTP as used
        otpToken.setUsed(true);
        otpTokenRepository.save(otpToken);

        // If verifying registration, mark email as verified
        if (request.getType() == OtpType.REGISTRATION) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            log.info("Email verified successfully for user: {}", request.getEmail());
        }

        log.info("OTP verified successfully for: {}", request.getEmail());
    }
}