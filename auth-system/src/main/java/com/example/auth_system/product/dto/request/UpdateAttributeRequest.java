package com.example.auth_system.product.dto.request;

import com.example.auth_system.product.enums.AttributeType;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttributeRequest {

    @Size(max = 100, message = "Attribute name must not exceed 100 characters")
    private String name;

    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    private AttributeType attributeType;

}