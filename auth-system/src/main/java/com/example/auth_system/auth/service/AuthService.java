package com.example.auth_system.auth.service;

import com.example.auth_system.auth.dto.request.ChangePasswordRequest;
import com.example.auth_system.auth.dto.request.ForgotPasswordRequest;
import com.example.auth_system.auth.dto.request.LoginRequest;
import com.example.auth_system.auth.dto.request.RefreshTokenRequest;
import com.example.auth_system.auth.dto.request.RegisterRequest;
import com.example.auth_system.auth.dto.request.ResetPasswordRequest;
import com.example.auth_system.auth.dto.request.SendOtpRequest;
import com.example.auth_system.auth.dto.request.VerifyOtpRequest;
import com.example.auth_system.auth.dto.response.AuthResponse;
import com.example.auth_system.auth.dto.response.LoginResponse;
import com.example.auth_system.auth.dto.response.RefreshTokenResponse;
import com.example.auth_system.auth.dto.response.UserInfoResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    void logout(String token);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    void sendOtp(SendOtpRequest request);
    void verifyOtp(VerifyOtpRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);  // Add this
    void verifyEmail(String token);  // Add this
    void changePassword(String authHeader, ChangePasswordRequest request); 
    UserInfoResponse getCurrentUser(String authHeader);
}