// src/main/java/com/example/auth_system/auth/service/PermissionService.java
package com.example.auth_system.auth.service;

import com.example.auth_system.auth.entity.User;

import java.util.Set;

public interface PermissionService {

    /**
     * Get all permission names for a user
     */
    Set<String> getUserPermissions(User user);

    /**
     * Check if user has a specific permission
     */
    boolean hasPermission(User user, String permissionName);

    /**
     * Check if user has ANY of the given permissions
     */
    boolean hasAnyPermission(User user, String... permissionNames);

    /**
     * Check if user has ALL of the given permissions
     */
    boolean hasAllPermissions(User user, String... permissionNames);
}