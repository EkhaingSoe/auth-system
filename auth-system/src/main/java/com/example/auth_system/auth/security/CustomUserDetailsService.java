// src/main/java/com/example/auth_system/auth/security/CustomUserDetailsService.java
package com.example.auth_system.auth.security;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.debug("Loading user by: {}", usernameOrEmail);

        // ✅ Find by email or username
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with: " + usernameOrEmail));

        log.debug("User found: {}", user.getEmail());

        // ✅ Return CustomUserDetails instead of User
        return new CustomUserDetails(user);
    }
}