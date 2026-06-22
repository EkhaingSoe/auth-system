// src/main/java/com/example/auth_system/brand/dto/request/UpdateBrandRequest.java
package com.example.auth_system.brand.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBrandRequest {
    private String name;
    private String description;
    private Boolean isActive;
}