package com.example.auth_system.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.auth_system.auth.entity.UserSessions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSessions, UUID> {

    Optional<UserSessions> findByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);

    List<UserSessions> findByUserId(UUID userId);

    List<UserSessions> findByUserIdAndRevokedFalse(UUID userId);

    Optional<UserSessions> findByRefreshTokenAndRevokedFalse(String refreshToken);

    @Modifying
    @Query("""
                UPDATE UserSessions s
                SET s.revoked = true
                WHERE s.refreshToken = :refreshToken
            """)
    void revokeByRefreshToken(String refreshToken);

    @Modifying
    @Query("""
                UPDATE UserSessions s
                SET s.revoked = true
                WHERE s.user.id = :userId
            """)
    void revokeAllByUserId(UUID userId);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);

}
