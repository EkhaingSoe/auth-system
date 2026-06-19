// src/main/java/com/example/auth_system/auth/service/PermissionManagementService.java
package com.example.auth_system.auth.service;

import com.example.auth_system.auth.entity.Permission;
import com.example.auth_system.auth.entity.Role;

import java.util.List;
import java.util.Set;

public interface PermissionManagementService {

    List<Permission> getAllPermissions();

    Set<Permission> getPermissionsByRole(String roleName);

    Role assignPermissionsToRole(String roleName, List<String> permissionNames);

    Role addPermissionToRole(String roleName, String permissionName);

    Role removePermissionFromRole(String roleName, String permissionName);
}