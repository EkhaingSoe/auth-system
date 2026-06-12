package com.example.auth_system.auth.repository;

import com.example.auth_system.auth.entity.Role;
import com.example.auth_system.auth.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // RoleName (Enum) ကို အသုံးပြု၍ Role ကို ရှာဖွေရန် method
    Optional<Role> findByName(RoleName name);
}
