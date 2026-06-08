package com.example.auth_system.service;

import com.example.auth_system.model.User;
import com.example.auth_system.repository.UserRepository;
import com.example.auth_system.dto.LoginResponse;
import com.example.auth_system.dto.RegisterRequest;
import com.example.auth_system.dto.UserResponse;
import com.example.auth_system.util.JwtUtil; // JwtUtil ကို import လုပ်ပါ

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // 1. Field ထည့်ပါ

    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public LoginResponse loginUser(RegisterRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
            return new LoginResponse(token, userResponse);
        }
        return null;
    }
}