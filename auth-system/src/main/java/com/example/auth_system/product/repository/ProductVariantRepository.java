package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    Optional<ProductVariant> findBySku(String sku);

    Optional<ProductVariant> findByBarcode(String barcode);

    List<ProductVariant> findByProductId(UUID productId);

    List<ProductVariant> findByProductIdAndIsActiveTrue(UUID productId);

    @Query("SELECT v FROM ProductVariant v WHERE v.stockQuantity <= v.reorderLevel AND v.isActive = true")
    List<ProductVariant> findVariantsNeedingReorder();

    @Query("SELECT v FROM ProductVariant v WHERE v.stockQuantity > 0 AND v.isActive = true")
    List<ProductVariant> findInStockVariants();

    @Modifying
    @Query("UPDATE ProductVariant v SET v.stockQuantity = v.stockQuantity + :quantity WHERE v.id = :variantId")
    int updateStockQuantity(@Param("variantId") UUID variantId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductVariant v SET v.reservedQuantity = v.reservedQuantity + :quantity WHERE v.id = :variantId")
    int updateReservedQuantity(@Param("variantId") UUID variantId, @Param("quantity") Integer quantity);

    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);
}