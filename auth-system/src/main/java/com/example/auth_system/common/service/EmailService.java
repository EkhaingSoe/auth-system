package com.example.auth_system.common.service;

import com.example.auth_system.auth.entity.OtpType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    // Remove JavaMailSender for now - comment it out
    private final JavaMailSender mailSender;
    
    // public void sendOtpEmail(String to, String otp, OtpType type) {
    //     String subject = getOtpSubject(type);
    //     String body = String.format("Your OTP for %s is: %s\n\nThis OTP will expire in 10 minutes.", 
    //             getTypeDescription(type), otp);
        
    //     // Just log the email for testing (no actual email sending)
    //     log.info("========== EMAIL SIMULATION ==========");
    //     log.info("To: {}", to);
    //     log.info("Subject: {}", subject);
    //     log.info("OTP: {}", otp);
    //     log.info("Body: {}", body);
    //     log.info("=====================================");
        
    //     System.out.println("\n╔════════════════════════════════════════╗");
    //     System.out.println("║        📧 EMAIL VERIFICATION          ║");
    //     System.out.println("╠════════════════════════════════════════╣");
    //     System.out.println("║ To:  " + to);
    //     System.out.println("║ OTP: " + otp);
    //     System.out.println("║ Type: " + type);
    //     System.out.println("╚════════════════════════════════════════╝\n");
    // }
    
    // public void sendPasswordResetEmail(String to, String resetToken) {
    //     String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
    //     String subject = "Password Reset Request";
    //     String body = String.format("Click the link below to reset your password:\n%s\n\nThis link will expire in 24 hours.", resetLink);
        
    //     log.info("========== PASSWORD RESET SIMULATION ==========");
    //     log.info("To: {}", to);
    //     log.info("Subject: {}", subject);
    //     log.info("Reset Token: {}", resetToken);
    //     log.info("Reset Link: {}", resetLink);
    //     log.info("==============================================");
        
    //     System.out.println("\n🔐 PASSWORD RESET TOKEN: " + resetToken);
    //     System.out.println("   Email: " + to + "\n");
    // }
    
    // public void sendPasswordResetConfirmationEmail(String to) {
    //     String subject = "Password Reset Successful";
    //     String body = "Your password has been successfully reset. If you didn't perform this action, please contact support immediately.";
        
    //     log.info("Password reset confirmation sent to: {}", to);
    //     System.out.println("✅ Password reset confirmation for: " + to);
    // }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String to, String otp, OtpType type) {
        String subject = getOtpSubject(type);
        String body = String.format("""
                Welcome to Auth System!
                
                Your verification code is: %s
                
                This code will expire in 10 minutes.
                
                If you didn't request this, please ignore this email.
                
                Best regards,
                Auth System Team
                """, otp);
        
        sendEmail(to, subject, body);
        log.info("OTP email sent to: {}", to);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String body = String.format("""
                Hello,
                
                We received a request to reset your password.
                
                Click the link below to reset your password:
                %s
                
                This link will expire in 1 hour.
                
                If you didn't request this, please ignore this email.
                
                Best regards,
                Auth System Team
                """, resetLink);
        
        sendEmail(to, subject, body);
        log.info("Password reset email sent to: {}", to);
    }

    public void sendPasswordResetConfirmationEmail(String to) {
        String subject = "Password Reset Successful";
        String body = """
                Hello,
                
                Your password has been successfully reset.
                
                If you didn't perform this action, please contact support immediately.
                
                Best regards,
                Auth System Team
                """;
        
        sendEmail(to, subject, body);
        log.info("Password reset confirmation sent to: {}", to);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(fromEmail);
            message.setReplyTo("noreply@authsystem.com");
            
            mailSender.send(message);
            log.info("✓ Email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("✗ Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
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