// src/main/java/com/example/auth_system/auth/service/PermissionManagementService.java
package com.example.auth_system.permission.service;

import com.example.auth_system.permission.dto.response.PermissionResponse;
import com.example.auth_system.permission.dto.response.RoleResponse;
import com.example.auth_system.permission.enums.RoleName;

import java.util.List;
import java.util.Set;

public interface PermissionManagementService {

    List<PermissionResponse> getAllPermissions();

    List<PermissionResponse> getPermissionsByRole(RoleName roleName);

    RoleResponse assignPermissionsToRole(RoleName roleName, List<String> permissionNames);

    RoleResponse addPermissionToRole(RoleName roleName, String permissionName);

    RoleResponse removePermissionFromRole(RoleName roleName, String permissionName);
}