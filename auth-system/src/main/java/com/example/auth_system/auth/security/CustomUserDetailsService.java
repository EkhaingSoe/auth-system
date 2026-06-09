package com.example.auth_system.auth.security;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Check if user is enabled
        if (!user.isEnabled()) {
            log.warn("User account is disabled: {}", email);
            throw new UsernameNotFoundException("User account is disabled");
        }
        
        // Check if email is verified
        if (!user.isEmailVerified()) {
            log.warn("User email not verified: {}", email);
            throw new UsernameNotFoundException("Email not verified");
        }
        
        return buildUserDetails(user);
    }
    
    // Load user by ID (for internal use)
    public UserDetails loadUserById(String userId) {
        log.debug("Loading user by id: {}", userId);
        
        // This would require a findById method in repository
        // User user = userRepository.findById(UUID.fromString(userId))
        //         .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        // return buildUserDetails(user);
        
        throw new UnsupportedOperationException("Method not implemented yet");
    }
    
    // Build Spring Security UserDetails object
    private UserDetails buildUserDetails(User user) {
        // Create authority/role
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);
        
        // Build UserDetails
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(!user.isEnabled())
                .disabled(!user.isEnabled())
                .build();
    }
}
