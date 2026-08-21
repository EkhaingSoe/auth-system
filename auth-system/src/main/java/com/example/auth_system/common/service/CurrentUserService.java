package com.example.auth_system.common.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrentUserService {
        private final UserRepository userRepository;

        public User getCurrentUser() {

                Authentication authentication = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                String username = authentication.getName();

                System.out.println("========== CURRENT USER DEBUG ==========");
                System.out.println("Authentication: " + authentication);
                System.out.println("Authentication name: " + authentication.getName());
                System.out.println("Principal: " + authentication.getPrincipal());
                System.out.println("========================================");

                return userRepository.findByUsernameAndDeletedFalse(username)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "User not found"));
        }
}
