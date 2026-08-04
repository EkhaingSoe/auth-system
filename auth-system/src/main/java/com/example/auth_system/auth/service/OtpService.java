package com.example.auth_system.auth.service;

import com.example.auth_system.auth.entity.OtpToken;
import com.example.auth_system.auth.enums.OtpType;
import com.example.auth_system.auth.mapper.AuthMapper;
import com.example.auth_system.auth.repository.OtpTokenRepository;
import com.example.auth_system.common.service.EmailService;
import com.example.auth_system.common.util.OtpGenerator;

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
    private final AuthMapper authMapper;

    @Transactional
    public void generateAndSendOtp(String email, OtpType type) {

        String otp = otpGenerator.generateOtp();
        otpTokenRepository.invalidateAllActiveOtps(email, type);
        OtpToken otpToken = authMapper.toOtpTokenEntity(
                email,
                otp,
                type,
                LocalDateTime.now().plusMinutes(10));

        otpTokenRepository.save(otpToken);
        emailService.sendOtpEmail(email, otp, type);
    }
}