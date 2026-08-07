// src/main/java/com/example/auth_system/user/service/UserManagementService.java
package com.example.auth_system.user.service;

import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateStaffUserRequest;
import com.example.auth_system.user.dto.request.UpdateStaffUserRequest;
import com.example.auth_system.user.dto.response.UserInfoResponse;
import com.example.auth_system.user.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface UserService {

    // Get all users
    Page<UserResponse> getAllUsers(Pageable pageable);

    // Get user by ID
    UserResponse getUserById(UUID id);

    // Get user by email
    UserResponse getUserByEmail(String email);

    // ✅ NEW: Get user by username
    UserResponse getUserByUsername(String username);

    // Create new user
    UserResponse createUser(CreateStaffUserRequest request);

    // Update user
    UserResponse updateUser(UUID id, UpdateStaffUserRequest request);

    // Assign roles to user
    UserResponse assignRoles(UUID id, AssignRoleRequest request);

    // Enable user
    void enableUser(UUID id);

    // Disable user
    void disableUser(UUID id);

    // Delete user
    void deleteUser(UUID id);

    // Search users
    Page<UserResponse> searchUsers(String searchTerm, Pageable pageable);

    // Get users by role
    Page<UserResponse> getUsersByRole(String roleName, Pageable pageable);

    // Get enabled users only
    Page<UserResponse> getEnabledUsers(Pageable pageable);

    UserInfoResponse getCurrentUser(String authHeader);
}