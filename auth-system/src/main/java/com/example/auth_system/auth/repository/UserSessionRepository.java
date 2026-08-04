package com.example.auth_system.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth_system.auth.entity.UserSessions;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSessions, UUID> {

}
