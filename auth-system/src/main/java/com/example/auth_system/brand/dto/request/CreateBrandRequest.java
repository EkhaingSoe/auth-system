// src/main/java/com/example/auth_system/brand/dto/request/CreateBrandRequest.java
package com.example.auth_system.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandRequest {

    @NotBlank(message = "Brand name is required")
    private String name;

    private String description;

    @Builder.Default
    private Boolean isActive = true;
}
