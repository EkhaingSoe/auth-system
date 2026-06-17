// src/main/java/com/example/auth_system/user/dto/request/UpdateUserRequest.java
package com.example.auth_system.user.dto.request;

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
    private Boolean enabled;
}
