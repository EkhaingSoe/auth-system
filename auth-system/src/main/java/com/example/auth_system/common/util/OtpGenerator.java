package com.example.auth_system.common.util;  // Fixed package

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class OtpGenerator {
    
    private final Random random = new SecureRandom();
    
    public String generateOtp() {
        return generateOtp(6); // Default 6 digits
    }
    
    public String generateOtp(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
    
    public boolean validateOtp(String otp, String expectedOtp) {
        return otp != null && otp.equals(expectedOtp);
    }
}
