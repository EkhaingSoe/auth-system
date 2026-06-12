package com.example.auth_system.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_system.auth.dto.request.ChangePasswordRequest;
import com.example.auth_system.auth.dto.request.ForgotPasswordRequest;
import com.example.auth_system.auth.dto.request.LoginRequest;
import com.example.auth_system.auth.dto.request.RefreshTokenRequest;
import com.example.auth_system.auth.dto.request.RegisterRequest;
import com.example.auth_system.auth.dto.request.SendOtpRequest;
import com.example.auth_system.auth.dto.request.VerifyOtpRequest;
import com.example.auth_system.auth.dto.response.AuthResponse;
import com.example.auth_system.auth.dto.response.LoginResponse;
import com.example.auth_system.auth.dto.response.RefreshTokenResponse;
import com.example.auth_system.auth.dto.response.UserInfoResponse;
import com.example.auth_system.auth.service.AuthService;
import com.example.auth_system.common.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    
    /**
     * Register a new user
     * @param request Registration request with email, password, first name, last name
     * @return AuthResponse with registration status
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Received registration request for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user
     * @param request Login request with email and password
     * @return LoginResponse with JWT tokens and user info
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

     /**
     * Logout user
     * @param request HttpServletRequest to extract Authorization header
     * @return ApiResponse with logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        log.info("Received logout request");
        String authHeader = request.getHeader("Authorization");
        authService.logout(authHeader);
        
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Logged out successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Request password reset
     * @param request ForgotPasswordRequest with email
     * @return ApiResponse with status
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Received forgot password request for email: {}", request.getEmail());
        authService.forgotPassword(request);
        
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Password reset link sent to your email if account exists")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(response);
    }

     /**
     * Send OTP to user's email
     * @param request SendOtpRequest with email and OTP type
     * @return ApiResponse with status
     */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        log.info("Received send OTP request for email: {} type: {}", request.getEmail(), request.getType());
        authService.sendOtp(request);
        
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("OTP sent successfully to your email")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Verify OTP code
     * @param request VerifyOtpRequest with email, OTP code, and type
     * @return ApiResponse with verification status
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("Received verify OTP request for email: {} type: {}", request.getEmail(), request.getType());
        authService.verifyOtp(request);
        
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("OTP verified successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(response);
    }

     /**
     * Refresh access token using refresh token
     * @param request RefreshTokenRequest with refresh token
     * @return RefreshTokenResponse with new tokens
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Received token refresh request");
        // Note: Implement this method in AuthService if needed
        // RefreshTokenResponse response = authService.refreshToken(request);
        
        // Temporary response - replace with actual service call
        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .accessToken("new_access_token_here")
                .refreshToken("new_refresh_token_here")
                .tokenType("Bearer")
                .expiresIn(3600)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Verify email using token (alternative to OTP)
     * @param token Email verification token
     * @return ApiResponse with verification status
     */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String token) {
        log.info("Received email verification request with token");
        // Note: Implement this method in AuthService if needed
        // authService.verifyEmail(token);
        
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Email verified successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Change password for authenticated user
     * @param request ChangePasswordRequest with old and new password
     * @param httpRequest HttpServletRequest to extract token
     * @return ApiResponse with status
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        log.info("Received change password request");
        String authHeader = httpRequest.getHeader("Authorization");
        // Note: Implement this method in AuthService if needed
        // authService.changePassword(authHeader, request);
        
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Password changed successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(response);
    }

     /**
     * Get current authenticated user info
     * @param httpRequest HttpServletRequest to extract token
     * @return UserInfoResponse with current user details
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(HttpServletRequest httpRequest) {
        log.info("Received get current user request");
        String authHeader = httpRequest.getHeader("Authorization");
        // Note: Implement this method in AuthService if needed
        // UserInfoResponse response = authService.getCurrentUser(authHeader);
        
        // Temporary response - replace with actual service call
        UserInfoResponse response = UserInfoResponse.builder()
                .id("1")
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .role("ROLE_USER")
                .emailVerified(true)
                .build();
        return ResponseEntity.ok(response);
    }


    
}
