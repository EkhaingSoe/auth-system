package com.example.auth_system.user.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private boolean emailVerified;
    private boolean enabled;
    private LocalDateTime createdAt; // Change from String to LocalDateTime
    private LocalDateTime lastLoginAt;
}
