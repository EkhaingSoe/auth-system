package com.example.auth_system.auth.controller;

import com.example.auth_system.auth.dto.request.*;
import com.example.auth_system.auth.dto.response.*;
import com.example.auth_system.auth.service.AuthService;
import com.example.auth_system.common.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     * 
     * @param request Registration request with email, password, first name, last
     *                name
     * @return AuthResponse with registration status
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("📝 POST /api/auth/register - Email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user
     * 
     * @param request Login request with email and password
     * @return LoginResponse with JWT tokens and user info
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("🔐 POST /api/auth/login - Email: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Logout user
     * 
     * @param request HttpServletRequest to extract Authorization header
     * @return ApiResponse with logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        log.info("🚪 POST /api/auth/logout - Logout user");
        String authHeader = request.getHeader("Authorization");
        authService.logout(authHeader);
        return ResponseEntity.ok(ApiResponse.success(200, "Logged out successfully", null));
    }

    /**
     * Request password reset
     * 
     * @param request ForgotPasswordRequest with email
     * @return ApiResponse with status
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("🔑 POST /api/auth/forgot-password - Email: {}", request.getEmail());
        authService.forgotPassword(request);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Password reset link sent to your email if account exists", null));
    }

    /**
     * Reset password using token
     * 
     * @param request ResetPasswordRequest with token, new password, confirm
     *                password
     * @return ApiResponse with reset status
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("🔒 POST /api/auth/reset-password - Reset password");
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Password reset successfully", null));
    }

    /**
     * Send OTP to user's email
     * 
     * @param request SendOtpRequest with email and OTP type
     * @return ApiResponse with status
     */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        log.info("📧 POST /api/auth/send-otp - Email: {}, Type: {}", request.getEmail(), request.getType());
        authService.sendOtp(request);
        return ResponseEntity.ok(ApiResponse.success(200, "OTP sent successfully", null));
    }

    /**
     * Verify OTP code
     * 
     * @param request VerifyOtpRequest with email, OTP code, and type
     * @return ApiResponse with verification status
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("✅ POST /api/auth/verify-otp - Email: {}, Type: {}", request.getEmail(), request.getType());
        authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success(200, "OTP verified successfully", null));
    }

    /**
     * Refresh access token using refresh token
     * 
     * @param request RefreshTokenRequest with refresh token
     * @return RefreshTokenResponse with new tokens
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("🔄 POST /api/auth/refresh-token - Refresh token");
        // TODO: Implement refresh token logic in AuthService
        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    /**
     * Verify email using token (alternative to OTP)
     * 
     * @param token Email verification token
     * @return ApiResponse with verification status
     */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        log.info("📧 GET /api/auth/verify-email - Verifying email");
        // TODO: Implement email verification logic
        authService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success(200, "Email verified successfully", null));
    }

    /**
     * Change password for authenticated user
     * 
     * @param request     ChangePasswordRequest with old and new password
     * @param httpRequest HttpServletRequest to extract token
     * @return ApiResponse with status
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        log.info("🔐 POST /api/auth/change-password - Change password");
        String authHeader = httpRequest.getHeader("Authorization");
        authService.changePassword(authHeader, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Password changed successfully", null));
    }

    /**
     * Get current authenticated user info
     * 
     * @param httpRequest HttpServletRequest to extract token
     * @return UserInfoResponse with current user details
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(HttpServletRequest request) {
        log.info("👤 GET /api/auth/me - Get current user");
        String authHeader = request.getHeader("Authorization");
        UserInfoResponse response = authService.getCurrentUser(authHeader);
        return ResponseEntity.ok(ApiResponse.success(200, "User retrieved successfully", response));
    }
}