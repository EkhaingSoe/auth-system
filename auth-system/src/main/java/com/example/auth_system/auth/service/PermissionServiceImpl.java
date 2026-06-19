// src/main/java/com/example/auth_system/auth/service/impl/PermissionServiceImpl.java
package com.example.auth_system.auth.service;

import com.example.auth_system.auth.entity.Permission;
import com.example.auth_system.auth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Override
    public Set<String> getUserPermissions(User user) {
        if (user == null || user.getRoles() == null) {
            return Set.of();
        }

        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasPermission(User user, String permissionName) {
        if (user == null || permissionName == null) {
            return false;
        }
        Set<String> permissions = getUserPermissions(user);
        boolean has = permissions.contains(permissionName);
        log.debug("User {} has permission {}: {}", user.getEmail(), permissionName, has);
        return has;
    }

    @Override
    public boolean hasAnyPermission(User user, String... permissionNames) {
        if (user == null || permissionNames == null || permissionNames.length == 0) {
            return false;
        }
        Set<String> permissions = getUserPermissions(user);
        for (String permission : permissionNames) {
            if (permissions.contains(permission)) {
                log.debug("User {} has any permission: {}", user.getEmail(), permission);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAllPermissions(User user, String... permissionNames) {
        if (user == null || permissionNames == null || permissionNames.length == 0) {
            return false;
        }
        Set<String> permissions = getUserPermissions(user);
        for (String permission : permissionNames) {
            if (!permissions.contains(permission)) {
                log.debug("User {} missing permission: {}", user.getEmail(), permission);
                return false;
            }
        }
        log.debug("User {} has all permissions: {}", user.getEmail(), (Object) permissionNames);
        return true;
    }
}