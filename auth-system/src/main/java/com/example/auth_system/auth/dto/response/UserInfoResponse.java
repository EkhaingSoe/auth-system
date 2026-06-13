package com.example.auth_system.auth.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private boolean emailVerified;
    private boolean enabled;
    private LocalDateTime createdAt;      // Change from String to LocalDateTime
    private LocalDateTime lastLoginAt;
}
