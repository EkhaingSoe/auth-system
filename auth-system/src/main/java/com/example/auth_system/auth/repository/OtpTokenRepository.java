package com.example.auth_system.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.OtpToken;
import com.example.auth_system.auth.entity.OtpType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, UUID> {
    
    // Find valid (not used, not expired) OTP
    Optional<OtpToken> findByEmailAndOtpAndTypeAndUsedFalseAndExpiresAtAfter(
        String email, 
        String otp, 
        OtpType type, 
        LocalDateTime now
    );
    
    // Find latest OTP for email and type
    Optional<OtpToken> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, OtpType type);
    
    // Check if OTP exists and is valid
    boolean existsByEmailAndOtpAndTypeAndUsedFalseAndExpiresAtAfter(
        String email, 
        String otp, 
        OtpType type, 
        LocalDateTime now
    );
    
    // Mark all OTPs for an email as used (when password is changed, etc.)
    @Modifying
    @Transactional
    @Query("UPDATE OtpToken o SET o.used = true WHERE o.email = :email AND o.type = :type AND o.used = false")
    void invalidateAllActiveOtps(@Param("email") String email, @Param("type") OtpType type);
    
    // Delete expired OTPs (for cleanup job)
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpToken o WHERE o.expiresAt < :now")
    int deleteExpiredOtps(@Param("now") LocalDateTime now);
    
    // Delete all OTPs for a user
    @Modifying
    @Transactional
    void deleteByEmail(String email);
    
    // Find all expired but unused OTPs
    @Query("SELECT o FROM OtpToken o WHERE o.expiresAt < :now AND o.used = false")
    List<OtpToken> findExpiredUnusedOtps(@Param("now") LocalDateTime now);
    
    // Count OTP attempts in last hour (for rate limiting)
    @Query("SELECT COUNT(o) FROM OtpToken o WHERE o.email = :email AND o.createdAt > :since")
    long countOtpsByEmailSince(@Param("email") String email, @Param("since") LocalDateTime since);
}
