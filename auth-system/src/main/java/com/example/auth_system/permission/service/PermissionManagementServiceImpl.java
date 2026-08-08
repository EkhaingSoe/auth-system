// src/main/java/com/example/auth_system/auth/service/impl/PermissionManagementServiceImpl.java
package com.example.auth_system.permission.service;

import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.permission.dto.response.PermissionResponse;
import com.example.auth_system.permission.dto.response.RoleResponse;
import com.example.auth_system.permission.entity.Permission;
import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.enums.RoleName;
import com.example.auth_system.permission.mapper.PermissionMapper;
import com.example.auth_system.permission.mapper.RoleMapper;
import com.example.auth_system.permission.repository.PermissionRepository;
import com.example.auth_system.permission.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionManagementServiceImpl implements PermissionManagementService {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final PermissionMapper permissionMapper;
        private final RoleMapper roleMapper;

        @Override
        @Transactional(readOnly = true)
        public List<PermissionResponse> getAllPermissions() {
                log.info("Fetching all permissions");
                return permissionRepository.findAllByOrderByNameAsc().stream().map(permissionMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PermissionResponse> getPermissionsByRole(RoleName roleName) {
                log.info("Fetching permissions for role: {}", roleName);
                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
                return role.getPermissions().stream().map(permissionMapper::toResponse)
                                .sorted(Comparator.comparing(PermissionResponse::getName)).toList();
        }

        // ============================================================
        // REPLACE ROLE PERMISSIONS
        // ============================================================

        @Override
        @Transactional
        public RoleResponse assignPermissionsToRole(
                        RoleName roleName,
                        List<String> permissionNames) {

                log.info("Assigning permissions to role: {}", roleName);

                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Role not found: " + roleName));

                Set<Permission> permissions = permissionNames.stream()
                                .map(permissionName -> permissionRepository.findByName(permissionName)
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Permission not found: " + permissionName)))
                                .collect(Collectors.toSet());

                role.setPermissions(permissions);

                log.info("Permissions assigned successfully to role: {}", roleName);

                return roleMapper.toResponse(role);
        }

        // ============================================================
        // ADD SINGLE PERMISSION
        // ============================================================

        @Override
        @Transactional
        public RoleResponse addPermissionToRole(
                        RoleName roleName,
                        String permissionName) {

                log.info(
                                "Adding permission {} to role: {}",
                                permissionName,
                                roleName);

                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Role not found: " + roleName));

                Permission permission = permissionRepository.findByName(permissionName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Permission not found: " + permissionName));

                role.addPermission(permission);

                log.info(
                                "Permission {} added successfully to role: {}",
                                permissionName,
                                roleName);

                return roleMapper.toResponse(role);
        }

        // ============================================================
        // REMOVE SINGLE PERMISSION
        // ============================================================

        @Override
        @Transactional
        public RoleResponse removePermissionFromRole(
                        RoleName roleName,
                        String permissionName) {

                log.info(
                                "Removing permission {} from role: {}",
                                permissionName,
                                roleName);

                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Role not found: " + roleName));

                Permission permission = permissionRepository.findByName(permissionName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Permission not found: " + permissionName));

                role.removePermission(permission);

                log.info(
                                "Permission {} removed successfully from role: {}",
                                permissionName,
                                roleName);

                return roleMapper.toResponse(role);
        }

}
