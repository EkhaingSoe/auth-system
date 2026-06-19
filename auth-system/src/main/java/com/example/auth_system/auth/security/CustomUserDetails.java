// src/main/java/com/example/auth_system/auth/security/CustomUserDetails.java
package com.example.auth_system.auth.security;

import com.example.auth_system.auth.entity.Permission;
import com.example.auth_system.auth.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Get roles as authorities (ROLE_ADMIN, ROLE_MANAGER, etc.)
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toSet());

        // Get permissions as authorities (PRODUCT_CREATE, ORDER_READ, etc.)
        user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return authorities;
    }

    @Override
    public String getUsername() {
        // Use username if exists, otherwise email
        return user.getUsername() != null ? user.getUsername() : user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ✅ Helper method to get user ID
    public String getUserId() {
        return user.getId().toString();
    }

    // ✅ Helper method to get user email
    public String getEmail() {
        return user.getEmail();
    }
}
