package com.example.auth_system.auth.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private UUID userId;

    private String email;

    private boolean emailVerificationRequired;
}
