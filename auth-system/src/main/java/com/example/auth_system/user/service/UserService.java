// src/main/java/com/example/auth_system/user/service/UserManagementService.java
package com.example.auth_system.user.service;

import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateStaffUserRequest;
import com.example.auth_system.user.dto.request.UpdateStaffUserRequest;
import com.example.auth_system.user.dto.response.UserInfoResponse;
import com.example.auth_system.user.dto.response.UserResponse;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface UserService {

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(UUID id);

    UserResponse getUserByEmail(String email);

    UserResponse getUserByUsername(String username);

    UserResponse createStaffUser(CreateStaffUserRequest request);

    UserResponse updateStaffUser(UUID id, UpdateStaffUserRequest request);

    UserResponse assignRoles(UUID id, AssignRoleRequest request);

    void enableUser(UUID id);

    void disableUser(UUID id);

    void deleteUser(UUID id);

    // Search users
    Page<UserResponse> searchUsers(String searchTerm, Pageable pageable);

    // Get users by role
    Page<UserResponse> getUsersByRole(String roleName, Pageable pageable);

    // Get enabled users only
    Page<UserResponse> getActiveUsers(Pageable pageable);

    UserInfoResponse getCurrentUser(String authHeader);
}