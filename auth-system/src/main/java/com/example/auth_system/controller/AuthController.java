package com.example.auth_system.controller;

import com.example.auth_system.dto.ApiResponse;
import com.example.auth_system.dto.LoginResponse;
import com.example.auth_system.dto.RegisterRequest;
import com.example.auth_system.dto.UserResponse;
import com.example.auth_system.model.User;
import com.example.auth_system.repository.UserRepository;
import com.example.auth_system.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterRequest request) {

       try {
            User savedUser = authService.registerUser(request);
            UserResponse userResponse = new UserResponse(savedUser.getId(), savedUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "User registered successfully!", userResponse));

    } catch (DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(409, "This username already exists!", null));
    }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody RegisterRequest request) {
        LoginResponse loginResponse = authService.loginUser(request);

        if (loginResponse != null) {
            return ResponseEntity.ok(
                new ApiResponse<>(200, "Login successful!", loginResponse)
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Invalid username or password!", null));
        }
    }
    
}
