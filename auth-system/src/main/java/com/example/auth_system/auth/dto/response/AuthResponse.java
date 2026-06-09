package com.example.auth_system.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    
    private boolean success;
    private String message;
    private String token;
    private String refreshToken;
    private String userId;
    private Object data; // For additional data
    
    // Static factory methods
    public static AuthResponse success(String message) {
        return AuthResponse.builder()
            .success(true)
            .message(message)
            .build();
    }
    
    public static AuthResponse success(String message, String token) {
        return AuthResponse.builder()
            .success(true)
            .message(message)
            .token(token)
            .build();
    }
    
    public static AuthResponse error(String message) {
        return AuthResponse.builder()
            .success(false)
            .message(message)
            .build();
    }
}
