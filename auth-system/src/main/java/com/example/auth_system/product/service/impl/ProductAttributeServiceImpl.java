package com.example.auth_system.product.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.product.dto.response.ProductAttributeResponse;
import com.example.auth_system.product.entity.ProductAttribute;
import com.example.auth_system.product.entity.ProductAttributeValue;
import com.example.auth_system.product.repository.ProductAttributeRepository;
import com.example.auth_system.product.repository.ProductAttributeValueRepository;
import com.example.auth_system.product.service.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository attributeRepository;
    private final ProductAttributeValueRepository valueRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttributeResponse> getActiveAttributesWithValues() {
        log.info("Fetching all active attributes with values");

        List<ProductAttribute> attributes = attributeRepository.findActiveAttributesWithValues();

        return attributes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttributeResponse> getAllAttributes() {
        log.info("Fetching all attributes with values");

        List<ProductAttribute> attributes = attributeRepository.findAllWithValues();

        return attributes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductAttributeResponse getAttributeById(UUID id) {
        log.info("Fetching attribute by id: {}", id);

        ProductAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id: " + id));

        return toResponse(attribute);
    }

    @Override
    public ProductAttributeResponse createAttribute(ProductAttribute attribute) {
        log.info("Creating new attribute: {}", attribute.getName());

        // Check for duplicate name
        if (attributeRepository.existsByName(attribute.getName())) {
            throw new BusinessException("Attribute with name already exists: " + attribute.getName());
        }

        ProductAttribute savedAttribute = attributeRepository.save(attribute);
        log.info("Attribute created successfully: {}", savedAttribute.getId());

        return toResponse(savedAttribute);
    }

    @Override
    public ProductAttributeResponse updateAttribute(UUID id, ProductAttribute attribute) {
        log.info("Updating attribute: {}", id);

        ProductAttribute existingAttribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id: " + id));

        // Check for duplicate name (if changed)
        if (!existingAttribute.getName().equals(attribute.getName())
                && attributeRepository.existsByName(attribute.getName())) {
            throw new BusinessException("Attribute with name already exists: " + attribute.getName());
        }

        existingAttribute.setName(attribute.getName());
        existingAttribute.setDisplayName(attribute.getDisplayName());
        existingAttribute.setAttributeType(attribute.getAttributeType());
        existingAttribute.setIsActive(attribute.getIsActive());

        ProductAttribute updatedAttribute = attributeRepository.save(existingAttribute);
        log.info("Attribute updated successfully: {}", updatedAttribute.getId());

        return toResponse(updatedAttribute);
    }

    @Override
    public void deleteAttribute(UUID id) {
        log.info("Deleting attribute: {}", id);

        ProductAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id: " + id));

        // Soft delete
        attribute.setIsActive(false);
        attributeRepository.save(attribute);
        log.info("Attribute deactivated successfully: {}", id);
    }

    @Override
    public ProductAttributeResponse addValueToAttribute(UUID attributeId, ProductAttributeValue value) {
        log.info("Adding value to attribute: {}", attributeId);

        ProductAttribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id: " + attributeId));

        // Check for duplicate value
        if (valueRepository.existsByAttributeIdAndValue(attributeId, value.getValue())) {
            throw new BusinessException("Value already exists for this attribute: " + value.getValue());
        }

        value.setAttribute(attribute);
        ProductAttributeValue savedValue = valueRepository.save(value);
        attribute.addValue(savedValue);

        log.info("Value added successfully: {}", savedValue.getId());
        return toResponse(attribute);
    }

    @Override
    public void removeValueFromAttribute(UUID attributeId, UUID valueId) {
        log.info("Removing value: {} from attribute: {}", valueId, attributeId);

        ProductAttributeValue value = valueRepository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException("Value not found with id: " + valueId));

        // Verify value belongs to attribute
        if (!value.getAttribute().getId().equals(attributeId)) {
            throw new BusinessException("Value does not belong to this attribute");
        }

        // Soft delete
        value.setIsActive(false);
        valueRepository.save(value);
        log.info("Value deactivated successfully: {}", valueId);
    }

    private ProductAttributeResponse toResponse(ProductAttribute attribute) {
        return ProductAttributeResponse.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .displayName(attribute.getDisplayName())
                .attributeType(attribute.getAttributeType() != null ? attribute.getAttributeType().name() : null)
                .isActive(attribute.getIsActive())
                .values(attribute.getValues().stream()
                        .filter(ProductAttributeValue::getIsActive)
                        .map(this::toValueResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private ProductAttributeResponse.AttributeValueResponse toValueResponse(ProductAttributeValue value) {
        return ProductAttributeResponse.AttributeValueResponse.builder()
                .id(value.getId())
                .value(value.getValue())
                .hexCode(value.getHexCode())
                .displayOrder(value.getDisplayOrder())
                .isActive(value.getIsActive())
                .build();
    }
}