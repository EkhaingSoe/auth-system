package com.example.auth_system.auth.repository;

import com.example.auth_system.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<Role, Long> {
    // Custom queries if needed
}
