package com.example.auth_system.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.PasswordResetToken;
import com.example.auth_system.auth.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    
    // Find by token (most common)
    Optional<PasswordResetToken> findByToken(String token);
    
    // Find valid (not used, not expired) token
    Optional<PasswordResetToken> findByTokenAndUsedFalseAndExpiresAtAfter(String token, LocalDateTime now);
    
    // Find by user
    Optional<PasswordResetToken> findByUser(User user);
    
    // Check if valid token exists for user
    boolean existsByUserAndUsedFalseAndExpiresAtAfter(User user, LocalDateTime now);
    
    // Invalidate all tokens for a user
    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetToken p SET p.used = true WHERE p.user = :user AND p.used = false")
    void invalidateAllUserTokens(@Param("user") User user);
    
    // Delete expired tokens (cleanup job)
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    // Find expired but unused tokens
    @Query("SELECT p FROM PasswordResetToken p WHERE p.expiresAt < :now AND p.used = false")
    List<PasswordResetToken> findExpiredUnusedTokens(@Param("now") LocalDateTime now);
    
    // Delete all tokens for a user
    @Modifying
    @Transactional
    void deleteByUser(User user);
}
