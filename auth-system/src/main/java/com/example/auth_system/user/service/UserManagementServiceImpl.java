// src/main/java/com/example/auth_system/user/service/impl/UserManagementServiceImpl.java
package com.example.auth_system.user.service;

import com.example.auth_system.auth.entity.Role;
import com.example.auth_system.auth.entity.RoleName;
import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.RoleRepository;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.exception.UserAlreadyExistsException;
import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateUserRequest;
import com.example.auth_system.user.dto.request.UpdateUserRequest;
import com.example.auth_system.user.dto.response.UserResponse;
import com.example.auth_system.user.mapper.UserMapper;
import com.example.auth_system.user.repository.UserManagementRepository;
import com.example.auth_system.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

    private final UserManagementRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with username: {}, email: {}", request.getUsername(), request.getEmail());

        // ✅ Check if username already exists
        if (request.getUsername() != null && userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username '" + request.getUsername() + "' already taken");
        }

        // ✅ Check if email already exists (if provided)
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + request.getEmail() + "' already in use");
        }

        // Map request to entity
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ✅ Assign role (single role from request)
        Set<Role> roles = new HashSet<>();
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            Role role = roleRepository.findByName(RoleName.valueOf(request.getRole()))
                    .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));
            roles.add(role);
        } else {
            // Default role if none specified
            Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));
            roles.add(defaultRole);
        }
        user.setRoles(roles);

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            user.setEmailVerified(true); // Staff auto-verified
            user.setEnabled(true); // Staff auto-enabled
        } else {
            user.setEmailVerified(false); // Public needs OTP
            user.setEnabled(false); // Public needs verification
        }

        user = userRepository.save(user);
        log.info("User created successfully with id: {}, username: {}, email: {}",
                user.getId(), user.getUsername(), user.getEmail());

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // ✅ Only update allowed fields
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }

        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        log.info("User updated successfully: {}", user.getEmail());
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse assignRoles(UUID id, AssignRoleRequest request) {
        log.info("Assigning roles to user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Get roles from request
        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        log.info("Roles assigned successfully to user: {}", user.getEmail());
        return userMapper.toResponse(user);
    }

    @Override
    public void enableUser(UUID id) {
        log.info("Enabling user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setEnabled(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User enabled successfully: {}", user.getEmail());
    }

    @Override
    public void disableUser(UUID id) {
        log.info("Disabling user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setEnabled(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User disabled successfully: {}", user.getEmail());
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        log.info("User deleted successfully: {}", user.getEmail());
    }

    @Override
    public List<UserResponse> searchUsers(String searchTerm) {
        log.info("Searching users with term: {}", searchTerm);

        List<User> users = userRepository.searchUsers(searchTerm);
        return userMapper.toResponseList(users);
    }

    @Override
    public List<UserResponse> getUsersByRole(String roleName) {
        log.info("Fetching users by role: {}", roleName);

        List<User> users = userRepository.findByRoleName(roleName);
        return userMapper.toResponseList(users);
    }

    @Override
    public List<UserResponse> getEnabledUsers() {
        log.info("Fetching enabled users");

        List<User> users = userRepository.findAllEnabled();
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toResponse(user);
    }
}