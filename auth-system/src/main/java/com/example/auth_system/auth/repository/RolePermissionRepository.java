package com.example.auth_system.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth_system.permission.entity.Role;

public interface RolePermissionRepository extends JpaRepository<Role, Long> {
    // Custom queries if needed
}
