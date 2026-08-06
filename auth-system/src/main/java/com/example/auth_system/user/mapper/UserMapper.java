// src/main/java/com/example/auth_system/user/mapper/UserMapper.java
package com.example.auth_system.user.mapper;

import com.example.auth_system.user.dto.request.CreateUserRequest;
import com.example.auth_system.user.dto.request.UpdateUserRequest;
import com.example.auth_system.user.dto.response.UserInfoResponse;
import com.example.auth_system.user.dto.response.UserResponse;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .username(request.getUsername()) // ✅ ADDED: Map username
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .build();
    }

    public User updateEntity(User user, UpdateUserRequest request) {
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        return user;
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .storeId(user.getStore() != null ? user.getStore().getId() : null)
                .storeName(user.getStore() != null ? user.getStore().getName() : null)
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()))
                .status(user.getStatus())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    public Page<UserResponse> toResponseList(Page<User> users) {
        return users.map(this::toResponse);
    }

    public UserInfoResponse toUserInfoResponse(User user) {

        String roleName = user.getRoles()
                .stream()
                .findFirst()
                .map(role -> role.getName().name())
                .orElse("ROLE_CUSTOMER");

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(roleName)
                .emailVerified(user.isEmailVerified())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}