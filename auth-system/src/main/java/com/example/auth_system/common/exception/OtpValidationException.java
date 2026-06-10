package com.example.auth_system.common.exception;

public class OtpValidationException extends RuntimeException {
    
    public OtpValidationException(String message) {
        super(message);
    }
    
    public OtpValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}