package com.example.auth_system.product.dto.request;

import com.example.auth_system.product.entity.ProductAttribute;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttributeRequest {

    @NotBlank(message = "Attribute name is required")
    private String name;

    private String displayName;

    @NotNull(message = "Attribute type is required")
    private ProductAttribute.AttributeType attributeType;
}