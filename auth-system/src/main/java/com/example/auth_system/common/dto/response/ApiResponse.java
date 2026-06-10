package com.example.auth_system.common.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse {
    private boolean success;
    private String message;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    // Optional: Add status code if needed
    private Integer statusCode;

     public static ApiResponse success(String message) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .build();
    }
    
    // Convenience method for error responses
    public static ApiResponse error(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}
