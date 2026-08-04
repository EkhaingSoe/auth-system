package com.example.auth_system.auth.service;

import com.example.auth_system.auth.dto.request.*;
import com.example.auth_system.auth.dto.response.*;
import com.example.auth_system.auth.entity.*;
import com.example.auth_system.auth.enums.OtpType;
import com.example.auth_system.auth.mapper.AuthMapper;
import com.example.auth_system.auth.repository.*;
import com.example.auth_system.auth.security.JwtTokenProvider;
import com.example.auth_system.common.exception.AuthException;
import com.example.auth_system.common.exception.InvalidCredentialsException;
import com.example.auth_system.common.exception.InvalidTokenException;
import com.example.auth_system.common.exception.OtpValidationException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.exception.UserAlreadyExistsException;
import com.example.auth_system.common.service.EmailService;
import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.entity.RoleName;
import com.example.auth_system.permission.repository.RoleRepository;
import com.example.auth_system.user.dto.response.UserInfoResponse;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;
import com.example.auth_system.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;

    // refactor finished
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository
                .findByName(RoleName.ROLE_CASHIER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = authMapper.toRegisterEntity(
                request,
                role,
                passwordEncoder);

        otpService.generateAndSendOtp(user.getEmail(), OtpType.REGISTRATION);
        User savedUser = userRepository.save(user);
        return authMapper.toRegisterResponse(savedUser);

    }

    // refactor finished
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .or(() -> userRepository.findByUsername(request.getEmail()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.isEmailVerified()) {
            throw new AuthException("Please verify your email before logging in");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthException("Account is disabled. Please contact support");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return authMapper.toLoginResponse(user);
    }

    @Override
    public void logout(String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtTokenProvider.invalidateToken(token);
        }

    }

    // refactor finished
    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        String resetToken = jwtTokenProvider.generatePasswordResetToken(user);

        PasswordResetToken passwordResetToken = authMapper.toPasswordResetToken(
                user,
                resetToken,
                LocalDateTime.now().plusHours(24));

        passwordResetTokenRepository.save(passwordResetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    // refactor finished
    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("Passwords do not match");
        }

        LocalDateTime now = LocalDateTime.now();
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByTokenAndUsedFalseAndExpiresAtAfter(request.getToken(), LocalDateTime.now())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(now);
        resetToken.setUsed(true);
        userRepository.save(user);
        passwordResetTokenRepository.save(resetToken);
        otpTokenRepository.invalidateAllActiveOtps(user.getEmail(), OtpType.PASSWORD_RESET);
        emailService.sendPasswordResetConfirmationEmail(user.getEmail());
    }

    // refactor finished this sendOtp is for sending OTP for resent Otp button
    @Override
    @Transactional
    public void sendOtp(SendOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        long otpCount = otpTokenRepository.countOtpsByEmailSince(
                request.getEmail(),
                LocalDateTime.now().minusHours(1));

        if (otpCount >= 5) {
            throw new AuthException("Too many OTP requests. Please try again later");
        }

        otpService.generateAndSendOtp(user.getEmail(), request.getType());
    }

    // refactor finished // later i will add featues for login , email verification
    // and password reset
    @Override
    @Transactional
    public void verifyOtp(VerifyOtpRequest request) {

        OtpToken otpToken = otpTokenRepository
                .findByEmailAndOtpAndTypeAndUsedFalseAndExpiresAtAfter(
                        request.getEmail(),
                        request.getOtp(),
                        request.getType(),
                        LocalDateTime.now())
                .orElseThrow(() -> new OtpValidationException("Invalid or expired OTP"));

        otpToken.setUsed(true);
        otpTokenRepository.save(otpToken);

        if (request.getType() == OtpType.REGISTRATION) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            user.setEmailVerified(true);
            user.setStatus(UserStatus.ACTIVE);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    public UserInfoResponse getCurrentUser(String authHeader) {

        String token = extractTokenFromHeader(authHeader);
        log.debug("Token: {}", token.substring(0, Math.min(token.length(), 50)) + "...");

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        log.info("🔍 Extracted email from token: {}", email); // ← ADD THIS

        // Debug: Check what users exist in database
        List<User> allUsers = userRepository.findAll();
        log.info("📊 Total users in DB: {}", allUsers.size());
        for (User u : allUsers) {
            log.info("   User: {} - {}", u.getEmail(), u.getId());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        log.info("✅ Found user: {} with ID: {}", user.getEmail(), user.getId());

        String roleName = user.getRoles().stream()
                .findFirst()
                .map(role -> role.getName().name())
                .orElse("ROLE_CUSTOMER");

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(roleName)
                .emailVerified(user.isEmailVerified())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newAccessToken = jwtTokenProvider.generateToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }

    @Override
    public void verifyEmail(String token) {
        log.info("Verifying email with token");
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        log.info("Email verified for user: {}", email);
    }

    @Override
    public void changePassword(String authHeader, ChangePasswordRequest request) {
        log.info("Changing password");

        String token = extractTokenFromHeader(authHeader);
        String email = jwtTokenProvider.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password changed for user: {}", email);
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException("Missing or invalid authorization header");
        }
        return authHeader.substring(7);
    }
}