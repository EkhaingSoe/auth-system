package com.example.auth_system.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth_system.permission.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
