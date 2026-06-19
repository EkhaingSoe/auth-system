// src/main/java/com/example/auth_system/user/controller/UserManagementController.java
package com.example.auth_system.user.controller;

import com.example.auth_system.auth.entity.RoleName;
import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateUserRequest;
import com.example.auth_system.user.dto.request.UpdateUserRequest;
import com.example.auth_system.user.dto.response.UserResponse;
import com.example.auth_system.user.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {

        private final UserManagementService userManagementService;

        // ============ GET ENDPOINTS ============

        @GetMapping
        public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
                log.info("GET /api/admin/users - Getting all users");
                List<UserResponse> users = userManagementService.getAllUsers();
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Users retrieved successfully", users));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
                log.info("GET /api/admin/users/{} - Getting user by id", id);
                UserResponse user = userManagementService.getUserById(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User retrieved successfully", user));
        }

        @GetMapping("/email/{email}")
        public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
                log.info("GET /api/admin/users/email/{} - Getting user by email", email);
                UserResponse user = userManagementService.getUserByEmail(email);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User retrieved successfully", user));
        }

        @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
                        @RequestParam String term) {
                log.info("GET /api/admin/users/search?term={} - Searching users", term);
                List<UserResponse> users = userManagementService.searchUsers(term);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Users found successfully", users));
        }

        @GetMapping("/role/{roleName}")
        public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(
                        @PathVariable("roleName") String roleName) {
                log.info("GET /api/admin/users/role/{} - Getting users by role", roleName);
                List<UserResponse> users = userManagementService.getUsersByRole(roleName);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Users retrieved successfully", users));
        }

        @GetMapping("/enabled")
        public ResponseEntity<ApiResponse<List<UserResponse>>> getEnabledUsers() {
                log.info("GET /api/admin/users/enabled - Getting enabled users");
                List<UserResponse> users = userManagementService.getEnabledUsers();
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Enabled users retrieved successfully", users));
        }

        // ============ POST ENDPOINTS ============

        @PostMapping
        public ResponseEntity<ApiResponse<UserResponse>> createUser(
                        @Valid @RequestBody CreateUserRequest request) {
                log.info("POST /api/admin/users - Creating new user: {}", request.getEmail());
                UserResponse user = userManagementService.createUser(request);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(201, "User created successfully", user));
        }

        // ============ PUT ENDPOINTS ============

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponse>> updateUser(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateUserRequest request) {
                log.info("PUT /api/admin/users/{} - Updating user", id);
                UserResponse user = userManagementService.updateUser(id, request);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User updated successfully", user));
        }

        @PutMapping("/{id}/roles")
        public ResponseEntity<ApiResponse<UserResponse>> assignRoles(
                        @PathVariable UUID id,
                        @Valid @RequestBody AssignRoleRequest request) {
                log.info("PUT /api/admin/users/{}/roles - Assigning roles: {}", id, request.getRoles());
                UserResponse user = userManagementService.assignRoles(id, request);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Roles assigned successfully", user));
        }

        @PutMapping("/{id}/enable")
        public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable UUID id) {
                log.info("PUT /api/admin/users/{}/enable - Enabling user", id);
                userManagementService.enableUser(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User enabled successfully", null));
        }

        @PutMapping("/{id}/disable")
        public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable UUID id) {
                log.info("PUT /api/admin/users/{}/disable - Disabling user", id);
                userManagementService.disableUser(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User disabled successfully", null));
        }

        // ============ DELETE ENDPOINTS ============

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
                log.info("DELETE /api/admin/users/{} - Deleting user", id);
                userManagementService.deleteUser(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "User deleted successfully", null));
        }
}