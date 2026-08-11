// src/main/java/com/example/auth_system/auth/service/impl/PermissionManagementServiceImpl.java
package com.example.auth_system.permission.service;

import com.example.auth_system.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
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
import java.util.HashSet;
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
        public RoleResponse assignPermissionsToRole(RoleName roleName, List<String> permissionNames) {

                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Role not found: " + roleName));

                Set<String> uniquePermissionNames = new HashSet<>(permissionNames);
                List<Permission> permissions = permissionRepository.findByNameIn(uniquePermissionNames);
                if (permissions.size() != uniquePermissionNames.size()) {
                        Set<String> foundPermissionNames = permissions.stream()
                                        .map(Permission::getName)
                                        .collect(Collectors.toSet());

                        String missingPermission = uniquePermissionNames.stream()
                                        .filter(name -> !foundPermissionNames.contains(name))
                                        .findFirst()
                                        .orElse("Unknown");

                        throw new ResourceNotFoundException(
                                        "Permission not found: " + missingPermission);
                }

                role.setPermissions(new HashSet<>(permissions));
                return roleMapper.toResponse(role);
        }

        // ============================================================
        // ADD SINGLE PERMISSION
        // ============================================================

        @Override
        @Transactional
        public RoleResponse addPermissionToRole(RoleName roleName, String permissionName) {

                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Role not found: " + roleName));

                Permission permission = permissionRepository.findByName(permissionName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Permission not found: " + permissionName));

                boolean alreadyAssigned = role.getPermissions()
                                .stream()
                                .anyMatch(existingPermission -> existingPermission.getId().equals(permission.getId()));

                if (alreadyAssigned) {
                        throw new BusinessException(
                                        "permission",
                                        "Permission already assigned to role: "
                                                        + roleName + " - " + permissionName,
                                        "PERMISSION_ALREADY_ASSIGNED",
                                        HttpStatus.CONFLICT);
                }

                role.addPermission(permission);
                return roleMapper.toResponse(role);
        }

        // ============================================================
        // REMOVE SINGLE PERMISSION
        // ============================================================

        @Override
        @Transactional
        public RoleResponse removePermissionFromRole(RoleName roleName, String permissionName) {

                Role role = roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Role not found: " + roleName));

                Permission permission = permissionRepository.findByName(permissionName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Permission not found: " + permissionName));

                boolean assigned = role.getPermissions()
                                .stream()
                                .anyMatch(existingPermission -> existingPermission.getId().equals(permission.getId()));

                if (!assigned) {
                        throw new ResourceNotFoundException(
                                        "Permission is not assigned to role: " + roleName + " - " + permissionName);
                }

                role.removePermission(permission);
                return roleMapper.toResponse(role);
        }

}
