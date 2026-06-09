package com.example.auth_system.auth.service;


import com.example.auth_system.auth.entity.OtpToken;
import com.example.auth_system.auth.entity.OtpType;
import com.example.auth_system.auth.repository.OtpTokenRepository;
import com.example.auth_system.util.OtpGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    
    private final OtpTokenRepository otpTokenRepository;
    private final OtpGenerator otpGenerator;
    private final EmailService emailService;
    
    @Transactional
    public void sendOtp(String email, OtpType type) {
        // Generate 6-digit OTP
        String otp = otpGenerator.generate();
        
        // Invalidate existing active OTPs for this email and type
        otpTokenRepository.invalidateAllActiveOtps(email, type);
        
        // Create new OTP token
        OtpToken otpToken = OtpToken.builder()
                .email(email)
                .otp(otp)
                .type(type)
                .used(false)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        
        otpTokenRepository.save(otpToken);
        
        // Send OTP via email
        emailService.sendOtpEmail(email, otp, type);
        
        log.info("OTP sent to {} for type {}", email, type);
    }
}