package com.example.auth_system.auth.service;



import com.example.auth_system.auth.entity.OtpType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    public void sendOtpEmail(String to, String otp, OtpType type) {
        String subject = getOtpSubject(type);
        String body = String.format("Your OTP for %s is: %s\n\nThis OTP will expire in 10 minutes.", 
                getTypeDescription(type), otp);
        
        sendEmail(to, subject, body);
    }
    
    public void sendPasswordResetEmail(String to, String resetToken) {
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        String subject = "Password Reset Request";
        String body = String.format("Click the link below to reset your password:\n%s\n\nThis link will expire in 24 hours.", resetLink);
        
        sendEmail(to, subject, body);
    }
    
    public void sendPasswordResetConfirmationEmail(String to) {
        String subject = "Password Reset Successful";
        String body = "Your password has been successfully reset. If you didn't perform this action, please contact support immediately.";
        
        sendEmail(to, subject, body);
    }
    
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("noreply@yourapp.com");
        
        mailSender.send(message);
        log.info("Email sent to: {}", to);
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