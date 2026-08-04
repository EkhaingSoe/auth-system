package com.example.auth_system.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.entity.RoleName;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    List<User> findByRolesContaining(Role role);

    @Query("""
                SELECT u
                FROM User u
                JOIN u.roles r
                WHERE r.name = :roleName
            """)
    List<User> findByRoleName(@Param("roleName") RoleName roleName);

    @Query("""
                SELECT u
                FROM User u
                WHERE u.status = :status
            """)
    List<User> findUsersByStatus(@Param("status") UserStatus status);

    @Query("""
                SELECT u
                FROM User u
                WHERE
                LOWER(u.username) LIKE LOWER(CONCAT('%',:keyword,'%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
                OR LOWER(u.firstName) LIKE LOWER(CONCAT('%',:keyword,'%'))
                OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<User> searchUsers(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("""
                UPDATE User u
                SET u.lastLoginAt = :time
                WHERE u.email = :email
            """)
    void updateLastLogin(@Param("email") String email, @Param("time") LocalDateTime time);

}
