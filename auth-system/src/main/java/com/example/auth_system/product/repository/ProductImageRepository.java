package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    List<ProductImage> findByProductId(UUID productId);

    List<ProductImage> findByVariantId(UUID variantId);

    List<ProductImage> findByProductIdOrderBySortOrderAsc(UUID productId);

    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(UUID productId);

    @Modifying
    @Query("UPDATE ProductImage i SET i.isPrimary = false WHERE i.product.id = :productId")
    void clearPrimaryImages(@Param("productId") UUID productId);

    @Modifying
    @Query("DELETE FROM ProductImage i WHERE i.product.id = :productId")
    void deleteByProductId(@Param("productId") UUID productId);
}