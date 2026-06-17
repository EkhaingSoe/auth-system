package com.example.auth_system.common.bootstrap;

import com.example.auth_system.auth.entity.Role;
import com.example.auth_system.auth.entity.RoleName;
import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.RoleRepository;
import com.example.auth_system.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

            Role adminRole = roleRepository
                    .findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(
                            passwordEncoder.encode("Admin@123"))
                    .firstName("System")
                    .lastName("Admin")
                    .enabled(true)
                    .emailVerified(true)
                    .roles(new HashSet<>())
                    .build();

            admin.getRoles().add(adminRole);

            userRepository.save(admin);

            System.out.println(
                    "Admin account created");
        }

    }
}