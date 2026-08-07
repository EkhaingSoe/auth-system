// src/main/java/com/example/auth_system/user/controller/UserManagementController.java
package com.example.auth_system.user.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateStaffUserRequest;
import com.example.auth_system.user.dto.request.UpdateStaffUserRequest;
import com.example.auth_system.user.dto.response.UserInfoResponse;
import com.example.auth_system.user.dto.response.UserResponse;
import com.example.auth_system.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

        private final UserService userService;

        // get end point

        @GetMapping
        public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
                Page<UserResponse> users = userService.getAllUsers(pageable);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Users retrieved successfully", users));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
                UserResponse user = userService.getUserById(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User retrieved successfully", user));
        }

        @GetMapping("/email/{email}")
        public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
                log.info("GET /api/admin/users/email/{} - Getting user by email", email);
                UserResponse user = userService.getUserByEmail(email);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User retrieved successfully", user));
        }

        @GetMapping("/search")
        public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
                        @RequestParam String term, Pageable pageable) {
                log.info("GET /api/admin/users/search?term={} - Searching users", term);
                Page<UserResponse> users = userService.searchUsers(term, pageable);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Users found successfully", users));
        }

        @GetMapping("/role/{roleName}")
        public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersByRole(
                        @PathVariable("roleName") String roleName, Pageable pageable) {
                log.info("GET /api/admin/users/role/{} - Getting users by role", roleName);
                Page<UserResponse> users = userService.getUsersByRole(roleName, pageable);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Users retrieved successfully", users));
        }

        @GetMapping("/enabled")
        public ResponseEntity<ApiResponse<Page<UserResponse>>> getEnabledUsers(Pageable pageable) {
                log.info("GET /api/admin/users/enabled - Getting enabled users");
                Page<UserResponse> users = userService.getActiveUsers(pageable);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Enabled users retrieved successfully", users));
        }

        // post end point

        @PostMapping
        public ResponseEntity<ApiResponse<UserResponse>> createUser(
                        @Valid @RequestBody CreateStaffUserRequest request) {
                log.info("POST /api/admin/users - Creating new user: {}", request.getEmail());
                UserResponse user = userService.createStaffUser(request);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(201, "User created successfully", user));
        }

        // ============ PUT ENDPOINTS ============

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponse>> updateUser(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateStaffUserRequest request) {
                log.info("PUT /api/admin/users/{} - Updating user", id);
                UserResponse user = userService.updateStaffUser(id, request);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User updated successfully", user));
        }

        @PutMapping("/{id}/roles")
        public ResponseEntity<ApiResponse<UserResponse>> assignRoles(
                        @PathVariable UUID id,
                        @Valid @RequestBody AssignRoleRequest request) {
                log.info("PUT /api/admin/users/{}/roles - Assigning roles: {}", id, request.getRoles());
                UserResponse user = userService.assignRoles(id, request);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Roles assigned successfully", user));
        }

        @PutMapping("/{id}/enable")
        public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable UUID id) {
                log.info("PUT /api/admin/users/{}/enable - Enabling user", id);
                userService.enableUser(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User enabled successfully", null));
        }

        @PutMapping("/{id}/disable")
        public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable UUID id) {
                log.info("PUT /api/admin/users/{}/disable - Disabling user", id);
                userService.disableUser(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User disabled successfully", null));
        }

        // ============ DELETE ENDPOINTS ============

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(HttpServletRequest request) {
                String authHeader = request.getHeader("Authorization");
                UserInfoResponse response = userService.getCurrentUser(authHeader);
                return ResponseEntity.ok(ApiResponse.success(200, "User retrieved successfully", response));
        }
}