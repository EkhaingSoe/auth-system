package com.example.auth_system.product.service;

import com.example.auth_system.product.dto.request.CreateAttributeRequest;
import com.example.auth_system.product.dto.request.UpdateAttributeRequest;
import com.example.auth_system.product.dto.request.UpdateAttributeValueRequest;
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

    ProductAttributeResponse createAttribute(CreateAttributeRequest request);

    ProductAttributeResponse updateAttribute(UUID id, UpdateAttributeRequest request);

    // Attribute Value Operations
    ProductAttributeResponse addValueToAttribute(UUID attributeId, ProductAttributeValue value);

    void deleteAttribute(UUID attributeId);

    void deactivateAttribute(UUID attributeId);

    void activateAttribute(UUID attributeId);

    ProductAttributeResponse updateAttributeValue(UUID attributeId, UUID valueId,
            UpdateAttributeValueRequest request);

    void deleteValue(UUID attributeId, UUID valueId);

    void deactivateValue(UUID attributeId, UUID valueId);

    void activateValue(UUID attributeId, UUID valueId);
}