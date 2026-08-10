// src/main/java/com/example/auth_system/auth/service/PermissionService.java
package com.example.auth_system.permission.service;

import java.util.Set;

import com.example.auth_system.user.entity.User;

public interface PermissionService {

    // this service is used internally in auth module

    Set<String> getUserPermissions(User user);

    boolean hasPermission(User user, String permissionName);

    boolean hasAnyPermission(User user, String... permissionNames);

    boolean hasAllPermissions(User user, String... permissionNames);
}