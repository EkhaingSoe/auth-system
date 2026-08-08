package com.example.auth_system.permission.repository;

import com.example.auth_system.permission.entity.Role;
import com.example.auth_system.permission.enums.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}
