package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAttributeValueRequest {

    @NotBlank(message = "Value is required")
    private String value;

    private String hexCode; // For color attributes: #FF0000

    private Integer displayOrder;
}