package com.example.auth_system.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.entity.RoleName;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmailAndDeletedFalse(String email);

    Optional<User> findByUsernameAndDeletedFalse(String username);

    @Query("""
                SELECT u
                FROM User u
                WHERE u.deleted = false
                AND (u.username = :keyword OR u.email = :keyword)
            """)
    Optional<User> findActiveUserByUsernameOrEmail(
            @Param("keyword") String keyword);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByUsernameAndDeletedFalse(String username);

    Optional<User> findByEmailAndStatusAndDeletedFalse(String email, UserStatus status);

    // For internal service logic
    List<User> findByRolesContaining(Role role);

    // For UI (pagination)
    @Query("""
                SELECT u
                FROM User u
                JOIN u.roles r
                WHERE r.name = :roleName
                AND u.deleted = false
            """)
    Page<User> findByRoleName(@Param("roleName") RoleName roleName, Pageable pageable);

    @Query("""
                SELECT u
                FROM User u
                WHERE u.status = :status
                AND u.deleted = false
            """)
    Page<User> findUsersByStatus(@Param("status") UserStatus status, Pageable pageable);

    @Query("""
                SELECT u
                FROM User u
                WHERE u.deleted = false
                AND (
                    LOWER(u.username) LIKE LOWER(CONCAT('%',:keyword,'%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
                    OR LOWER(u.firstName) LIKE LOWER(CONCAT('%',:keyword,'%'))
                    OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:keyword,'%'))
                )
            """)
    Page<User> searchUsers(
            @Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
                UPDATE User u
                SET u.lastLoginAt = :time
                WHERE u.email = :email
            """)
    void updateLastLogin(@Param("email") String email, @Param("time") LocalDateTime time);

    @Query("""
                SELECT DISTINCT u
                FROM User u
                LEFT JOIN FETCH u.roles r
                LEFT JOIN FETCH r.permissions
                WHERE u.email = :email
                AND u.deleted = false
            """)
    Optional<User> findByEmailWithRoles(
            @Param("email") String email);

    @Query("""
                SELECT DISTINCT u
                FROM User u
                LEFT JOIN FETCH u.roles r
                LEFT JOIN FETCH r.permissions
                WHERE u.username = :username
                AND u.deleted = false
            """)
    Optional<User> findByUsernameWithRoles(
            @Param("username") String username);

    Optional<User> findByIdAndDeletedFalse(UUID id);

}
