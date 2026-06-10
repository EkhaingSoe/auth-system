package com.example.auth_system.auth.dto.response;

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
    private String createdAt;
    private String lastLoginAt;
}
