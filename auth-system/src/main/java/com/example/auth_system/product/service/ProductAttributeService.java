package com.example.auth_system.product.service;

import com.example.auth_system.product.dto.response.ProductAttributeResponse;
import com.example.auth_system.product.entity.ProductAttribute;
import com.example.auth_system.product.entity.ProductAttributeValue;

import java.util.List;
import java.util.UUID;

public interface ProductAttributeService {

    // Attribute Operations
    List<ProductAttributeResponse> getActiveAttributesWithValues();

    List<ProductAttributeResponse> getAllAttributes();

    ProductAttributeResponse getAttributeById(UUID id);

    ProductAttributeResponse createAttribute(ProductAttribute attribute);

    ProductAttributeResponse updateAttribute(UUID id, ProductAttribute attribute);

    void deleteAttribute(UUID id);

    // Attribute Value Operations
    ProductAttributeResponse addValueToAttribute(UUID attributeId, ProductAttributeValue value);

    // ProductAttributeResponse updateAttributeValue(UUID attributeId, UUID valueId,
    // ProductAttributeValue value);

    void removeValueFromAttribute(UUID attributeId, UUID valueId);
}