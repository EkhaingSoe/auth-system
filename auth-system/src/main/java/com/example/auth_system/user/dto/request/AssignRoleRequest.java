// src/main/java/com/example/auth_system/user/dto/request/AssignRoleRequest.java
package com.example.auth_system.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {

    @NotEmpty(message = "At least one role is required")
    private List<String> roles;
}
