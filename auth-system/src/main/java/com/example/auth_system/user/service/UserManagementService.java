// src/main/java/com/example/auth_system/user/service/UserManagementService.java
package com.example.auth_system.user.service;

import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateUserRequest;
import com.example.auth_system.user.dto.request.UpdateUserRequest;
import com.example.auth_system.user.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserManagementService {

    // Get all users
    List<UserResponse> getAllUsers();

    // Get user by ID
    UserResponse getUserById(UUID id);

    // Get user by email
    UserResponse getUserByEmail(String email);

    // Create new user
    UserResponse createUser(CreateUserRequest request);

    // Update user
    UserResponse updateUser(UUID id, UpdateUserRequest request);

    // Assign roles to user
    UserResponse assignRoles(UUID id, AssignRoleRequest request);

    // Enable user
    void enableUser(UUID id);

    // Disable user
    void disableUser(UUID id);

    // Delete user
    void deleteUser(UUID id);

    // Search users
    List<UserResponse> searchUsers(String searchTerm);

    // Get users by role
    List<UserResponse> getUsersByRole(String roleName);

    // Get enabled users only
    List<UserResponse> getEnabledUsers();
}