package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, UUID> {

    Optional<ProductAttribute> findByName(String name);

    List<ProductAttribute> findByIsActiveTrue();

    List<ProductAttribute> findByAttributeType(ProductAttribute.AttributeType attributeType);

    @Query("SELECT pa FROM ProductAttribute pa LEFT JOIN FETCH pa.values WHERE pa.isActive = true ORDER BY pa.displayName")
    List<ProductAttribute> findActiveAttributesWithValues();

    @Query("SELECT pa FROM ProductAttribute pa LEFT JOIN FETCH pa.values ORDER BY pa.displayName")
    List<ProductAttribute> findAllWithValues(); // ← ADD THIS METHOD

    boolean existsByName(String name);

    List<ProductAttribute> findByIsActiveTrueOrderByDisplayName();
}