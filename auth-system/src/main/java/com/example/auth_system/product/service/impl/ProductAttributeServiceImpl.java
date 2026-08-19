package com.example.auth_system.product.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.product.dto.request.CreateAttributeRequest;
import com.example.auth_system.product.dto.request.UpdateAttributeRequest;
import com.example.auth_system.product.dto.request.UpdateAttributeValueRequest;
import com.example.auth_system.product.dto.response.AttributeValueResponse;
import com.example.auth_system.product.dto.response.ProductAttributeResponse;
import com.example.auth_system.product.entity.ProductAttribute;
import com.example.auth_system.product.entity.ProductAttributeValue;
import com.example.auth_system.product.mapper.ProductAttributeMapper;
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
    private final ProductAttributeMapper attributeMapper;

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
    public ProductAttributeResponse createAttribute(CreateAttributeRequest request) {

        // Check for duplicate name
        if (attributeRepository.existsByName(request.getName())) {
            throw new BusinessException("Attribute with name already exists: " + request.getName());
        }

        ProductAttribute attribute = attributeMapper.toEntity(request);
        ProductAttribute savedAttribute = attributeRepository.save(attribute);
        log.info("Attribute created successfully: {}", savedAttribute.getId());

        return toResponse(savedAttribute);
    }

    @Override
    public ProductAttributeResponse updateAttribute(UUID id, UpdateAttributeRequest request) {
        log.info("Updating attribute: {}", id);

        ProductAttribute existingAttribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id: " + id));

        // Update name only when provided
        if (request.getName() != null && !request.getName().equals(existingAttribute.getName())) {

            if (attributeRepository.existsByName(request.getName())) {
                throw BusinessException.duplicate(
                        "name",
                        "Attribute",
                        request.getName());
            }

            existingAttribute.setName(request.getName());
        }

        if (request.getDisplayName() != null) {
            existingAttribute.setDisplayName(request.getDisplayName());
        }

        if (request.getAttributeType() != null) {
            existingAttribute.setAttributeType(request.getAttributeType());
        }

        ProductAttribute updatedAttribute = attributeRepository.save(existingAttribute);
        return toResponse(updatedAttribute);
    }

    @Override
    public void deleteAttribute(UUID id) {

        ProductAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id: " + id));

        attributeRepository.delete(attribute);
    }

    @Override
    @Transactional
    public void deactivateAttribute(UUID id) {

        ProductAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute not found with id: " + id));

        attribute.setIsActive(false);

        attributeRepository.save(attribute);
    }

    @Override
    @Transactional
    public void activateAttribute(UUID id) {

        ProductAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute not found with id: " + id));

        attribute.setIsActive(true);

        attributeRepository.save(attribute);
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
    @Transactional
    public ProductAttributeResponse updateAttributeValue(UUID attributeId, UUID valueId,
            UpdateAttributeValueRequest request) {

        ProductAttribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute not found with id: " + attributeId));

        ProductAttributeValue existingValue = valueRepository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute value not found with id: " + valueId));

        if (!existingValue.getAttribute().getId().equals(attributeId)) {
            throw new BusinessException(
                    "Value does not belong to this attribute");
        }

        if (request.getValue() != null
                && !request.getValue().equals(existingValue.getValue())) {

            existingValue.setValue(request.getValue());
        }

        if (request.getHexCode() != null) {
            existingValue.setHexCode(request.getHexCode());
        }

        if (request.getDisplayOrder() != null) {
            existingValue.setDisplayOrder(request.getDisplayOrder());
        }

        valueRepository.save(existingValue);
        return toResponse(attribute);
    }

    @Override
    public void deleteValue(UUID attributeId, UUID valueId) {

        ProductAttributeValue value = valueRepository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException("Value not found with id: " + valueId));

        if (!value.getAttribute().getId().equals(attributeId)) {
            throw new BusinessException("Value does not belong to this attribute");
        }

        valueRepository.delete(value);
    }

    @Override
    @Transactional
    public void deactivateValue(UUID attributeId, UUID valueId) {

        ProductAttributeValue value = valueRepository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute value not found with id: " + valueId));

        // Make sure the value belongs to the specified attribute
        if (value.getAttribute() == null || !value.getAttribute().getId().equals(attributeId)) {
            throw new ResourceNotFoundException("Attribute value does not belong to attribute: " + attributeId);
        }

        if (!Boolean.TRUE.equals(value.getIsActive())) {
            throw new BusinessException("Attribute value is already inactive");
        }

        value.setIsActive(false);
        valueRepository.save(value);
    }

    @Override
    @Transactional
    public void activateValue(UUID attributeId, UUID valueId) {

        ProductAttributeValue value = valueRepository.findById(valueId).orElseThrow(() -> new ResourceNotFoundException(
                "Attribute value not found with id: " + valueId));

        // Make sure the value belongs to the specified attribute
        if (value.getAttribute() == null || !value.getAttribute().getId().equals(attributeId)) {
            throw new ResourceNotFoundException("Attribute value does not belong to attribute: " + attributeId);
        }

        if (Boolean.TRUE.equals(value.getIsActive())) {
            throw new BusinessException("Attribute value is already active");
        }

        value.setIsActive(true);
        valueRepository.save(value);
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

    private AttributeValueResponse toValueResponse(ProductAttributeValue value) {
        return AttributeValueResponse.builder()
                .id(value.getId())
                .value(value.getValue())
                .hexCode(value.getHexCode())
                .displayOrder(value.getDisplayOrder())
                .isActive(value.getIsActive())
                .build();
    }
}