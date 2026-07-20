package com.example.auth_system.inventory.repository;

import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, UUID> {

    Optional<WarehouseStock> findByProductAndVariantAndWarehouse(Product product, ProductVariant variant,
            Store warehouse);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WarehouseStock> findByProductIdAndVariantIdAndWarehouseId(UUID productId, UUID variantId,
            UUID warehouseId);

    List<WarehouseStock> findByWarehouseId(UUID warehouseId);

    List<WarehouseStock> findByProductId(UUID productId);

    List<WarehouseStock> findByVariantId(UUID variantId);

    boolean existsByProductAndVariantAndWarehouse(
            Product product,
            ProductVariant variant,
            Store warehouse);

    @Query("SELECT ws FROM WarehouseStock ws WHERE ws.currentQuantity <= ws.reorderLevel AND ws.currentQuantity > 0")
    List<WarehouseStock> findItemsBelowReorderLevel();

    @Query("SELECT ws FROM WarehouseStock ws WHERE ws.currentQuantity = 0")
    List<WarehouseStock> findOutOfStockItems();

    @Query("SELECT ws FROM WarehouseStock ws WHERE ws.currentQuantity > ws.maxStock")
    List<WarehouseStock> findOverStockItems();

    @Query("SELECT ws FROM WarehouseStock ws WHERE ws.warehouse.id = :warehouseId AND ws.currentQuantity <= ws.reorderLevel")
    List<WarehouseStock> findWarehouseItemsBelowReorder(@Param("warehouseId") UUID warehouseId);

    @Modifying
    @Query("UPDATE WarehouseStock ws SET ws.currentQuantity = :quantity, ws.lastUpdatedAt = CURRENT_TIMESTAMP WHERE ws.id = :id")
    int updateStockQuantity(@Param("id") UUID id, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE WarehouseStock ws SET ws.reservedQuantity = :reserved WHERE ws.id = :id")
    int updateReservedQuantity(@Param("id") UUID id, @Param("reserved") Integer reserved);
}