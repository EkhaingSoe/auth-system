// src/main/java/com/example/auth_system/user/service/impl/UserManagementServiceImpl.java
package com.example.auth_system.user.service;

import com.example.auth_system.auth.security.JwtTokenProvider;
import com.example.auth_system.common.exception.AuthException;
import com.example.auth_system.common.exception.InvalidTokenException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.exception.UserAlreadyExistsException;
import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.enums.RoleName;
import com.example.auth_system.permission.repository.RoleRepository;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.repository.StoreRepository;
import com.example.auth_system.user.dto.request.AssignRoleRequest;
import com.example.auth_system.user.dto.request.CreateStaffUserRequest;
import com.example.auth_system.user.dto.request.UpdateStaffUserRequest;
import com.example.auth_system.user.dto.response.UserInfoResponse;
import com.example.auth_system.user.dto.response.UserResponse;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.enums.UserStatus;
import com.example.auth_system.user.enums.UserType;
import com.example.auth_system.user.mapper.UserMapper;
import com.example.auth_system.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByDeletedFalse(pageable);
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createStaffUser(CreateStaffUserRequest request) {

        if (request.getUsername() != null && userRepository.existsByUsernameAndDeletedFalse(request.getUsername())) {
            throw new UserAlreadyExistsException("Username '" + request.getUsername() + "' already taken");
        }

        if (request.getEmail() != null && userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + request.getEmail() + "' already in use");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(UserType.STAFF);

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Store not found: " + request.getStoreId()));

        user.setStore(store);

        Role role = roleRepository.findByName(
                RoleName.valueOf(request.getRole())).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Role not found: " + request.getRole()));

        user.setRoles(Set.of(role));

        // Staff account settings
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        log.info(
                "Staff created successfully id={}, username={}",
                savedUser.getId(),
                savedUser.getUsername());

        return userMapper.toResponse(savedUser);

        // Set<Role> roles = new HashSet<>();
        // if (request.getRole() != null && !request.getRole().isEmpty()) {
        // Role role = roleRepository.findByName(RoleName.valueOf(request.getRole()))
        // .orElseThrow(() -> new RuntimeException("Role not found: " +
        // request.getRole()));
        // roles.add(role);
        // } else {
        // Role defaultRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
        // .orElseThrow(() -> new RuntimeException("Default role ROLE_CUSTOMER not
        // found"));
        // roles.add(defaultRole);
        // }
        // user.setRoles(roles);
        // Store store = storeRepository.findById(request.getStoreId())
        // .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: "
        // + request.getStoreId()));

        // if (user.getUsername() != null && !user.getUsername().isEmpty()) {
        // if (request.getStoreId() == null) {
        // throw new RuntimeException("Store ID is required for staff users");
        // }

        // user.setStore(store);
        // user.setEmailVerified(true); // Staff auto-verified
        // user.setStatus(UserStatus.ACTIVE); // Staff auto-enabled
        // } else {
        // user.setStore(store);
        // user.setEmailVerified(false); // Public needs OTP
        // user.setStatus(UserStatus.PENDING); // Public needs verification
        // }

        // user = userRepository.save(user);
        // log.info("User created successfully with id: {}, username: {}, email: {}",
        // user.getId(), user.getUsername(), user.getEmail());

        // return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateStaffUser(UUID id, UpdateStaffUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse assignRoles(UUID id, AssignRoleRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void enableUser(UUID id) {
        log.info("Enabling user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(UserStatus.ACTIVE);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User enabled successfully: {}", user.getEmail());
    }

    @Override
    public void disableUser(UUID id) {
        log.info("Disabling user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(UserStatus.INACTIVE);
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
    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        log.info("Searching users with term: {}", searchTerm);

        Page<User> users = userRepository.searchUsers(searchTerm, pageable);
        return userMapper.toResponseList(users);
    }

    @Override
    public Page<UserResponse> getUsersByRole(String roleName, Pageable pageable) {
        log.info("Fetching users by role: {}", roleName);

        try {
            // ✅ Convert String to RoleName enum
            RoleName role = RoleName.valueOf(roleName);
            Page<User> users = userRepository.findByRoleName(role, pageable);
            return userMapper.toResponseList(users);
        } catch (IllegalArgumentException e) {
            log.error("Invalid role name: {}", roleName);
            throw new RuntimeException("Role not found: " + roleName);
        }
    }

    @Override
    public Page<UserResponse> getActiveUsers(Pageable pageable) {
        log.info("Fetching active users");

        Page<User> users = userRepository.findUsersByStatus(UserStatus.ACTIVE, pageable);
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toResponse(user);
    }

    @Override
    public UserInfoResponse getCurrentUser(String authHeader) {

        String token = extractTokenFromHeader(authHeader);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toUserInfoResponse(user);
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException("Missing or invalid authorization header");
        }
        return authHeader.substring(7);
    }
}