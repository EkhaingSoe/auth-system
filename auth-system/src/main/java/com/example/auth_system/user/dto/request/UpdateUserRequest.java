// src/main/java/com/example/auth_system/user/dto/request/UpdateUserRequest.java
package com.example.auth_system.user.dto.request;

import java.util.UUID;

import com.example.auth_system.user.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private UserStatus status;
    private UUID storeId;
}
