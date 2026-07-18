package com.example.auth_system.common.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import com.example.auth_system.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"));
    }
}
