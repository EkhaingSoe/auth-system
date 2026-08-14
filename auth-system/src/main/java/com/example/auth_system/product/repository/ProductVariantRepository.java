package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    Optional<ProductVariant> findBySku(String sku);

    Optional<ProductVariant> findByBarcode(String barcode);

    List<ProductVariant> findByProductId(UUID productId);

    List<ProductVariant> findByProductIdAndIsActiveTrue(UUID productId);

    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);
}