package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttributeValueRequest {

    @NotBlank(message = "Attribute value is required")
    @Size(max = 100, message = "Attribute value must not exceed 100 characters")
    private String value;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Hex code must be a valid 6-digit hexadecimal color")
    private String hexCode;

    @Builder.Default
    private Integer displayOrder = 0;
}