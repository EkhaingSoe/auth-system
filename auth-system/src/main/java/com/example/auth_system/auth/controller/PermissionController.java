// src/main/java/com/example/auth_system/auth/controller/PermissionController.java
package com.example.auth_system.auth.controller;

import com.example.auth_system.auth.entity.Permission;
import com.example.auth_system.auth.entity.Role;
import com.example.auth_system.auth.service.PermissionManagementService;
import com.example.auth_system.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionManagementService permissionManagementService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Permission>>> getAllPermissions() {
        log.info("GET /api/admin/permissions - Getting all permissions");
        List<Permission> permissions = permissionManagementService.getAllPermissions();
        return ResponseEntity.ok(
                ApiResponse.success(200, "Permissions retrieved successfully", permissions));
    }

    @GetMapping("/{roleName}/permission")
    public ResponseEntity<ApiResponse<Set<Permission>>> getPermissionsByRole(
            @PathVariable String roleName) {
        log.info("GET /api/admin/permissions/{}/permission - Getting permissions by role", roleName);

        Set<Permission> permissions = permissionManagementService.getPermissionsByRole(roleName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Permissions retrieved successfully", permissions));
    }

    @PutMapping("/{roleName}/permissions")
    public ResponseEntity<ApiResponse<Role>> assignPermissionsToRole(
            @PathVariable String roleName,
            @RequestBody List<String> permissionNames) {
        log.info("PUT /api/admin/permissions/{}/permissions - Assigning permissions: {}", roleName, permissionNames);

        Role role = permissionManagementService.assignPermissionsToRole(roleName, permissionNames);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Permissions assigned successfully", role));
    }

    @PostMapping("/role/{roleName}/permission/{permissionName}")
    public ResponseEntity<ApiResponse<Role>> addPermissionToRole(
            @PathVariable String roleName,
            @PathVariable String permissionName) {
        log.info("POST /api/admin/permissions/role/{}/permission/{} - Adding permission to role",
                roleName, permissionName);

        Role role = permissionManagementService.addPermissionToRole(roleName, permissionName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Permission added successfully", role));
    }

    @DeleteMapping("/role/{roleName}/permission/{permissionName}")
    public ResponseEntity<ApiResponse<Role>> removePermissionFromRole(
            @PathVariable String roleName,
            @PathVariable String permissionName) {
        log.info("DELETE /api/admin/permissions/role/{}/permission/{} - Removing permission from role",
                roleName, permissionName);

        Role role = permissionManagementService.removePermissionFromRole(roleName, permissionName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Permission removed successfully", role));
    }
}