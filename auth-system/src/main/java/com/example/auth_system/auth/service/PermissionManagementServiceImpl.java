// src/main/java/com/example/auth_system/auth/service/impl/PermissionManagementServiceImpl.java
package com.example.auth_system.auth.service;

import com.example.auth_system.auth.entity.Permission;
import com.example.auth_system.auth.entity.Role;
import com.example.auth_system.auth.entity.RoleName;
import com.example.auth_system.auth.repository.PermissionRepository;
import com.example.auth_system.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionManagementServiceImpl implements PermissionManagementService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<Permission> getAllPermissions() {
        log.info("Fetching all permissions");
        return permissionRepository.findAll();
    }

    @Override
    public Set<Permission> getPermissionsByRole(String roleName) {
        log.info("Fetching permissions for role: {}", roleName);

        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        return role.getPermissions();
    }

    @Override
    @Transactional
    public Role assignPermissionsToRole(String roleName, List<String> permissionNames) {
        log.info("Assigning permissions to role: {}", roleName);

        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        Set<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Permission not found: " + name)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        role = roleRepository.save(role);

        log.info("Permissions assigned successfully to role: {}", roleName);
        return role;
    }

    @Override
    @Transactional
    public Role addPermissionToRole(String roleName, String permissionName) {
        log.info("Adding permission {} to role: {}", permissionName, roleName);

        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));

        role.getPermissions().add(permission);
        role = roleRepository.save(role);

        log.info("Permission added successfully to role: {}", roleName);
        return role;
    }

    @Override
    @Transactional
    public Role removePermissionFromRole(String roleName, String permissionName) {
        log.info("Removing permission {} from role: {}", permissionName, roleName);

        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));

        role.getPermissions().remove(permission);
        role = roleRepository.save(role);

        log.info("Permission removed successfully from role: {}", roleName);
        return role;
    }
}
