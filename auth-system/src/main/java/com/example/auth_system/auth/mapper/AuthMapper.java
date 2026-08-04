package com.example.auth_system.auth.mapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth_system.auth.dto.request.RegisterRequest;
import com.example.auth_system.auth.dto.response.LoginResponse;
import com.example.auth_system.auth.dto.response.RegisterResponse;
import com.example.auth_system.auth.entity.OtpToken;
import com.example.auth_system.auth.entity.PasswordResetToken;
import com.example.auth_system.auth.enums.OtpType;
import com.example.auth_system.auth.security.JwtTokenProvider;
import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthMapper {

        private final JwtTokenProvider jwtTokenProvider;

        public User toRegisterEntity(RegisterRequest request, Role role, PasswordEncoder passwordEncoder) {

                return User.builder()
                                .email(request.getEmail())
                                .password(
                                                passwordEncoder.encode(request.getPassword()))
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .roles(Set.of(role))
                                .emailVerified(false)
                                .status(UserStatus.INACTIVE)
                                .build();
        }

        public RegisterResponse toRegisterResponse(User user) {

                return RegisterResponse.builder()
                                .userId(user.getId())
                                .email(user.getEmail())
                                .emailVerificationRequired(!user.isEmailVerified())
                                .build();
        }

        public LoginResponse toLoginResponse(User user) {

                String accessToken = jwtTokenProvider.generateToken(user);
                String refreshToken = jwtTokenProvider.generateRefreshToken(user);

                return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtTokenProvider.getExpirationTime())
                                .user(
                                                LoginResponse.UserInfo.builder()
                                                                .id(user.getId().toString())
                                                                .email(user.getEmail())
                                                                .firstName(user.getFirstName())
                                                                .lastName(user.getLastName())
                                                                .roles(
                                                                                user.getRoles()
                                                                                                .stream()
                                                                                                .map(role -> role
                                                                                                                .getName()
                                                                                                                .name())
                                                                                                .collect(Collectors
                                                                                                                .toList()))
                                                                .emailVerified(user.isEmailVerified())
                                                                .build())
                                .build();
        }

        public PasswordResetToken toPasswordResetToken(User user, String token, LocalDateTime expiresAt) {
                return PasswordResetToken.builder()
                                .token(token)
                                .user(user)
                                .used(false)
                                .expiresAt(expiresAt)
                                .build();
        }

        public OtpToken toOtpTokenEntity(String email, String otp, OtpType type, LocalDateTime expiresAt) {

                return OtpToken.builder()
                                .email(email)
                                .otp(otp)
                                .type(type)
                                .used(false)
                                .expiresAt(expiresAt)
                                .build();
        }
}
