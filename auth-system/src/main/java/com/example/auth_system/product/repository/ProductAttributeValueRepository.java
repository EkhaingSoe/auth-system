package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, UUID> {

    List<ProductAttributeValue> findByAttributeIdOrderByDisplayOrderAsc(UUID attributeId);

    List<ProductAttributeValue> findByAttributeIdAndIsActiveTrueOrderByDisplayOrderAsc(
            UUID attributeId);

    List<ProductAttributeValue> findByAttributeName(String attributeName);

    boolean existsByAttributeIdAndValue(UUID attributeId, String value);
}