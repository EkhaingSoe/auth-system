package com.example.auth_system.inventory.repository;

import com.example.auth_system.inventory.entity.InventoryCountItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryCountItemRepository extends JpaRepository<InventoryCountItem, UUID> {

    List<InventoryCountItem> findByInventoryCountId(UUID inventoryCountId);

    List<InventoryCountItem> findByProductId(UUID productId);

    List<InventoryCountItem> findByVariantId(UUID variantId);

    Optional<InventoryCountItem> findByInventoryCountIdAndProductId(UUID inventoryCountId, UUID productId);

    Optional<InventoryCountItem> findByInventoryCountIdAndProductIdAndVariantId(
            UUID inventoryCountId,
            UUID productId,
            UUID variantId);

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.difference > 0")
    List<InventoryCountItem> findItemsWithPositiveDifference();

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.difference < 0")
    List<InventoryCountItem> findItemsWithNegativeDifference();

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.difference != 0")
    List<InventoryCountItem> findDiscrepancyItemsByCountId(@Param("countId") UUID countId);

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.stockAdjustment IS NOT NULL")
    List<InventoryCountItem> findItemsWithStockAdjustment();

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.stockAdjustment.id = :adjustmentId")
    Optional<InventoryCountItem> findByStockAdjustmentId(UUID adjustmentId);

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.stockAdjustment IS NULL AND ci.difference != 0")
    List<InventoryCountItem> findItemsWithoutStockAdjustment();

    @Query("SELECT ci FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.stockAdjustment IS NULL AND ci.difference != 0")
    List<InventoryCountItem> findUnadjustedItemsByCountId(@Param("countId") UUID countId);

    long countByInventoryCountId(UUID inventoryCountId);

    @Query("SELECT COUNT(ci) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.difference != 0")
    long countDiscrepancyItemsByCountId(@Param("countId") UUID countId);

    @Query("SELECT COUNT(ci) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.difference > 0")
    long countPositiveDiscrepancyItemsByCountId(@Param("countId") UUID countId);

    @Query("SELECT COUNT(ci) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.difference < 0")
    long countNegativeDiscrepancyItemsByCountId(@Param("countId") UUID countId);

    @Query("SELECT COUNT(ci) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.stockAdjustment IS NOT NULL")
    long countAdjustedItemsByCountId(@Param("countId") UUID countId);

    @Query("SELECT COUNT(ci) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.stockAdjustment IS NULL")
    long countUnadjustedItemsByCountId(@Param("countId") UUID countId);

    @Query("SELECT COALESCE(SUM(ci.systemQuantity), 0) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId")
    Integer sumSystemQuantityByCountId(@Param("countId") UUID countId);

    @Query("SELECT COALESCE(SUM(ci.countedQuantity), 0) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId")
    Integer sumCountedQuantityByCountId(@Param("countId") UUID countId);

    @Query("SELECT COALESCE(SUM(ci.difference), 0) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId")
    Integer sumDifferenceByCountId(@Param("countId") UUID countId);

    @Query("SELECT COALESCE(SUM(ci.difference), 0) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.difference > 0")
    Integer sumPositiveDifferenceByCountId(@Param("countId") UUID countId);

    @Query("SELECT COALESCE(SUM(ci.difference), 0) FROM InventoryCountItem ci WHERE ci.inventoryCount.id = :countId AND ci.difference < 0")
    Integer sumNegativeDifferenceByCountId(@Param("countId") UUID countId);

    @Query("SELECT ci FROM InventoryCountItem ci JOIN FETCH ci.product WHERE ci.inventoryCount.id = :countId")
    List<InventoryCountItem> findWithProductByCountId(@Param("countId") UUID countId);

    @Query("SELECT ci FROM InventoryCountItem ci JOIN FETCH ci.product p LEFT JOIN FETCH ci.variant v WHERE ci.inventoryCount.id = :countId")
    List<InventoryCountItem> findWithProductAndVariantByCountId(@Param("countId") UUID countId);

    @Query("SELECT ci FROM InventoryCountItem ci JOIN FETCH ci.stockAdjustment WHERE ci.inventoryCount.id = :countId")
    List<InventoryCountItem> findWithStockAdjustmentByCountId(@Param("countId") UUID countId);

    @Query("SELECT ci FROM InventoryCountItem ci JOIN FETCH ci.product p LEFT JOIN FETCH ci.variant v LEFT JOIN FETCH ci.stockAdjustment sa WHERE ci.id = :itemId")
    Optional<InventoryCountItem> findWithAllDetailsById(@Param("itemId") UUID itemId);

    @Query("""
                SELECT COUNT(ci)
                FROM InventoryCountItem ci
                WHERE ci.inventoryCount.id = :countId
                AND ci.countedQuantity IS NULL
            """)
    long countUncountedItems(@Param("countId") UUID countId);

}