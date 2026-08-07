package com.example.auth_system.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.auth_system.user.enums.UserStatus;
import com.example.auth_system.user.enums.UserType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UserType userType;
    private List<String> roles;
    private boolean emailVerified;
    private UserStatus status;
    private LocalDateTime createdAt; // Change from String to LocalDateTime
    private LocalDateTime lastLoginAt;
}
