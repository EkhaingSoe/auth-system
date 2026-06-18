package com.example.auth_system.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.Role;
import com.example.auth_system.auth.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find by email (most common)
    Optional<User> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find by email and enabled status
    Optional<User> findByEmailAndEnabledTrue(String email);

    // Find users by role
    List<User> findByRolesContaining(Role role);

    // Find users created after certain date
    List<User> findByCreatedAtAfter(LocalDateTime date);

    // Custom query - update last login time
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.email = :email")
    void updateLastLoginByEmail(@Param("email") String email, @Param("lastLoginAt") LocalDateTime lastLoginAt);

    // Find users with email not verified
    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.createdAt < :cutoffDate")
    List<User> findUnverifiedUsersCreatedBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Soft delete or disable user
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled = false WHERE u.email = :email")
    void disableUserByEmail(@Param("email") String email);

    // Count active users
    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true AND u.emailVerified = true")
    long countActiveUsers();

    // Find by name containing (case insensitive)
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    // For login (both email and username)
    Optional<User> findByUsernameOrEmail(String username, String email);
}
