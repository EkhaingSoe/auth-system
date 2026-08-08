package com.example.auth_system.permission.mapper;

import com.example.auth_system.permission.dto.response.PermissionResponse;
import com.example.auth_system.permission.dto.response.RoleResponse;
import com.example.auth_system.permission.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final PermissionMapper permissionMapper;

    public RoleResponse toResponse(Role role) {
        if (role == null) {
            return null;
        }
        List<PermissionResponse> permissions = role.getPermissions().stream().map(permissionMapper::toResponse)
                .toList();
        return RoleResponse.builder().id(role.getId()).name(role.getName().name()).permissions(permissions)
                .permissionCount(permissions.size()).build();
    }
}
