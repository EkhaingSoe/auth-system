// src/main/java/com/example/auth_system/auth/controller/PermissionController.java
package com.example.auth_system.permission.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.permission.dto.request.AssignPermissionsRequest;
import com.example.auth_system.permission.dto.response.PermissionResponse;
import com.example.auth_system.permission.dto.response.RoleResponse;
import com.example.auth_system.permission.entity.Permission;
import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.enums.RoleName;
import com.example.auth_system.permission.service.PermissionManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

        private final PermissionManagementService permissionManagementService;

        @GetMapping
        public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
                log.info("GET /api/admin/permissions - Getting all permissions");
                List<PermissionResponse> permissions = permissionManagementService.getAllPermissions();
                return ResponseEntity.ok(ApiResponse.success(200, "Permissions retrieved successfully", permissions));
        }

        @GetMapping("/roles/{roleName}")
        public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByRole(
                        @PathVariable RoleName roleName) {
                log.info("GET /api/admin/permissions/roles/{} - Getting permissions by role", roleName);
                List<PermissionResponse> permissions = permissionManagementService.getPermissionsByRole(roleName);
                return ResponseEntity.ok(ApiResponse.success(200, "Permissions retrieved successfully", permissions));
        }

        @PutMapping("/roles/{roleName}/permissions")
        public ResponseEntity<ApiResponse<RoleResponse>> assignPermissionsToRole(@PathVariable RoleName roleName,
                        @Valid @RequestBody AssignPermissionsRequest request) {
                log.info("PUT /api/admin/permissions/roles/{}/permissions - Assigning permissions: {}", roleName,
                                request.getPermissionNames());
                RoleResponse role = permissionManagementService.assignPermissionsToRole(roleName,
                                request.getPermissionNames());
                return ResponseEntity.ok(ApiResponse.success(200, "Permissions assigned successfully", role));
        }

        @PostMapping("/roles/{roleName}/permissions/{permissionName}")
        public ResponseEntity<ApiResponse<RoleResponse>> addPermissionToRole(@PathVariable RoleName roleName,
                        @PathVariable String permissionName) {
                log.info("POST /api/admin/permissions/roles/{}/permissions/{}", roleName, permissionName);
                RoleResponse role = permissionManagementService.addPermissionToRole(roleName, permissionName);
                return ResponseEntity.ok(ApiResponse.success(200, "Permission added successfully", role));
        }

        @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
        public ResponseEntity<ApiResponse<RoleResponse>> removePermissionFromRole(@PathVariable RoleName roleName,
                        @PathVariable String permissionName) {
                log.info("DELETE /api/admin/permissions/roles/{}/permissions/{}", roleName, permissionName);
                RoleResponse role = permissionManagementService.removePermissionFromRole(roleName, permissionName);
                return ResponseEntity.ok(ApiResponse.success(200, "Permission removed successfully", role));
        }
}