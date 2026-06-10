package com.example.auth_system.common.service;

import com.example.auth_system.auth.entity.OtpType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    // Remove JavaMailSender for now - comment it out
    // private final JavaMailSender mailSender;
    
    public void sendOtpEmail(String to, String otp, OtpType type) {
        String subject = getOtpSubject(type);
        String body = String.format("Your OTP for %s is: %s\n\nThis OTP will expire in 10 minutes.", 
                getTypeDescription(type), otp);
        
        // Just log the email for testing (no actual email sending)
        log.info("========== EMAIL SIMULATION ==========");
        log.info("To: {}", to);
        log.info("Subject: {}", subject);
        log.info("OTP: {}", otp);
        log.info("Body: {}", body);
        log.info("=====================================");
        
        // Print to console for easy visibility during testing
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        📧 EMAIL VERIFICATION          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ To:  " + to);
        System.out.println("║ OTP: " + otp);
        System.out.println("║ Type: " + type);
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    public void sendPasswordResetEmail(String to, String resetToken) {
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        String subject = "Password Reset Request";
        String body = String.format("Click the link below to reset your password:\n%s\n\nThis link will expire in 24 hours.", resetLink);
        
        log.info("========== PASSWORD RESET SIMULATION ==========");
        log.info("To: {}", to);
        log.info("Subject: {}", subject);
        log.info("Reset Token: {}", resetToken);
        log.info("Reset Link: {}", resetLink);
        log.info("==============================================");
        
        System.out.println("\n🔐 PASSWORD RESET TOKEN: " + resetToken);
        System.out.println("   Email: " + to + "\n");
    }
    
    public void sendPasswordResetConfirmationEmail(String to) {
        String subject = "Password Reset Successful";
        String body = "Your password has been successfully reset. If you didn't perform this action, please contact support immediately.";
        
        log.info("Password reset confirmation sent to: {}", to);
        System.out.println("✅ Password reset confirmation for: " + to);
    }
    
    private String getOtpSubject(OtpType type) {
        switch (type) {
            case REGISTRATION:
                return "Verify Your Email";
            case PASSWORD_RESET:
                return "Password Reset OTP";
            case LOGIN:
                return "Login Verification Code";
            default:
                return "Your OTP Code";
        }
    }
    
    private String getTypeDescription(OtpType type) {
        switch (type) {
            case REGISTRATION:
                return "email verification";
            case PASSWORD_RESET:
                return "password reset";
            case LOGIN:
                return "login";
            default:
                return "verification";
        }
    }
}