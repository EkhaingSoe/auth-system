package com.example.auth_system.auth.dto.request;

import com.example.auth_system.auth.entity.OtpType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendOtpRequest {
    
     @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "OTP type is required")
    private OtpType type; // REGISTRATION, PASSWORD_RESET, LOGIN
    
    private String channel = "EMAIL"; // EMAIL or SMS
}
