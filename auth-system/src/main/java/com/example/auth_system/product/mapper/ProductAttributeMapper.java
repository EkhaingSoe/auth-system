package com.example.auth_system.product.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.product.dto.request.CreateAttributeRequest;
import com.example.auth_system.product.entity.ProductAttribute;

@Component
public class ProductAttributeMapper {

    public ProductAttribute toEntity(CreateAttributeRequest request) {
        return ProductAttribute.builder()
                .name(request.getName())
                .displayName(
                        request.getDisplayName() != null
                                ? request.getDisplayName()
                                : request.getName())
                .attributeType(request.getAttributeType())
                .isActive(true)
                .build();
    }

}
