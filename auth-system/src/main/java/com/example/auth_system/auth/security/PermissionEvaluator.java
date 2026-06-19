// src/main/java/com/example/auth_system/auth/security/PermissionEvaluator.java
package com.example.auth_system.auth.security;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("permission")
@RequiredArgsConstructor
@Slf4j
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    private final PermissionService permissionService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        String permissionName = permission.toString();

        boolean hasPermission = permissionService.hasPermission(user, permissionName);
        log.debug("User {} has permission {}: {}", user.getEmail(), permissionName, hasPermission);
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        // For object-level permissions (not used in your current setup)
        return hasPermission(authentication, null, permission);
    }

    // ✅ Keep your helper methods for convenience
    public boolean hasPermission(String permissionName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return hasPermission(auth, null, permissionName);
    }

    public boolean hasAnyPermission(String... permissionNames) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        return permissionService.hasAnyPermission(user, permissionNames);
    }
}