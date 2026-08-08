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
import com.example.auth_system.common.util.DeviceUtils;
import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.enums.RoleName;
import com.example.auth_system.permission.repository.RoleRepository;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;
import com.example.auth_system.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
    private final UserSessionRepository userSessionRepository;

    // refactor finished
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
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
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .or(() -> userRepository.findByUsernameAndDeletedFalse(request.getEmail()))
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

        String accessToken = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        String deviceName = DeviceUtils.getDeviceName(userAgent);

        UserSessions session = UserSessions.builder()
                .user(user)
                .refreshToken(refreshToken)
                .deviceName(deviceName)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .lastUsedAt(LocalDateTime.now())
                .build();

        userSessionRepository.save(session);

        return authMapper.toLoginResponse(
                user,
                accessToken,
                refreshToken);
    }

    @Override
    @Transactional
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

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
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

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
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
            User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            user.setEmailVerified(true);
            user.setStatus(UserStatus.ACTIVE);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserSessions session = userSessionRepository
                .findByRefreshTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Session not found"));

        String newAccessToken = jwtTokenProvider.generateToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
        Date expiryDate = jwtTokenProvider.getExpirationDateFromToken(newRefreshToken);
        LocalDateTime expiresAt = expiryDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        session.setRefreshToken(newRefreshToken);
        session.setLastUsedAt(LocalDateTime.now());
        session.setExpiresAt(expiresAt);
        session.setRevoked(false);

        userSessionRepository.save(session);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (user.isEmailVerified()) {
            throw new AuthException("Email is already verified");
        }

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

    }

    @Override
    public void changePassword(String authHeader, ChangePasswordRequest request) {

        String token = extractTokenFromHeader(authHeader);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid or expired token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);

        User user = userRepository.findByEmailAndDeletedFalse(email)
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

    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException("Missing or invalid authorization header");
        }
        return authHeader.substring(7);
    }
}