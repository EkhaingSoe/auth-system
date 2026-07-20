package com.example.auth_system.inventory.repository;

import com.example.auth_system.inventory.entity.StockMovement;
import com.example.auth_system.inventory.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    Optional<StockMovement> findByMovementNumber(String movementNumber);

    List<StockMovement> findByProductId(UUID productId);

    List<StockMovement> findByVariantId(UUID variantId);

    List<StockMovement> findByMovementType(MovementType movementType);

    List<StockMovement> findByReferenceId(UUID referenceId);

    List<StockMovement> findByFromWarehouseId(UUID warehouseId);

    List<StockMovement> findByToWarehouseId(UUID warehouseId);

    List<StockMovement> findByProductIdAndMovementType(UUID productId, MovementType movementType);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.createdAt BETWEEN :start AND :end")
    List<StockMovement> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.product.id = :productId AND sm.variant.id = :variantId")
    List<StockMovement> findByProductAndVariant(@Param("productId") UUID productId, @Param("variantId") UUID variantId);

    @Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM StockMovement sm WHERE sm.product.id = :productId AND sm.movementType IN :inTypes")
    Integer getTotalInQuantity(@Param("productId") UUID productId, @Param("inTypes") List<MovementType> inTypes);

    @Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM StockMovement sm WHERE sm.product.id = :productId AND sm.movementType IN :outTypes")
    Integer getTotalOutQuantity(@Param("productId") UUID productId, @Param("outTypes") List<MovementType> outTypes);
}