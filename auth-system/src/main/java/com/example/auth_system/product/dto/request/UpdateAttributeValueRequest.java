package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttributeValueRequest {

    @Size(max = 100, message = "Value must not exceed 100 characters")
    private String value;

    @Size(max = 7, message = "Hex code must not exceed 7 characters")
    private String hexCode;

    private Integer displayOrder;

}