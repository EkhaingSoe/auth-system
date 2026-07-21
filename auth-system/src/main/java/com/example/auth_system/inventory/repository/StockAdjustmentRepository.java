package com.example.auth_system.inventory.repository;

import com.example.auth_system.inventory.entity.StockAdjustment;
import com.example.auth_system.inventory.enums.AdjustmentStatus;
import com.example.auth_system.inventory.enums.AdjustmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, UUID> {

    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);

    List<StockAdjustment> findByProductId(UUID productId);

    List<StockAdjustment> findByVariantId(UUID variantId);

    List<StockAdjustment> findByWarehouseId(UUID warehouseId);

    List<StockAdjustment> findByAdjustmentType(AdjustmentType adjustmentType);

    List<StockAdjustment> findByStatus(AdjustmentStatus status);

    List<StockAdjustment> findByProductIdAndWarehouseId(UUID productId, UUID warehouseId);

    List<StockAdjustment> findByCreatedAtBetween(LocalDateTime start,
            LocalDateTime end);

    List<StockAdjustment> findByApprovedAtBetween(LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'PENDING' ORDER BY sa.createdAt ASC")
    List<StockAdjustment> findPendingAdjustments();

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'APPROVED'")
    List<StockAdjustment> findApprovedAdjustments();

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'COMPLETED'")
    List<StockAdjustment> findCompletedAdjustments();

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'REJECTED'")
    List<StockAdjustment> findRejectedAdjustments();

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.warehouse.id =:warehouseId AND sa.status = 'PENDING'")
    List<StockAdjustment> findPendingByWarehouseId(@Param("warehouseId") UUID warehouseId);

    List<StockAdjustment> findByStatusAndWarehouseId(AdjustmentStatus status, UUID warehouseId);

    List<StockAdjustment> findByAdjustmentTypeAndStatus(AdjustmentType type,
            AdjustmentStatus status);

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType IN ('DAMAGE', 'EXPIRY', 'THEFT')")
    List<StockAdjustment> findDamageAdjustments();

    long countByStatus(String status);

    long countByAdjustmentType(AdjustmentType type);

    long countByWarehouseId(UUID warehouseId);

    @Query("SELECT COUNT(sa) FROM StockAdjustment sa WHERE sa.status ='PENDING'")
    long countPendingAdjustments();

    @Query("SELECT COUNT(sa) FROM StockAdjustment sa WHERE sa.status ='COMPLETED'")
    long countCompletedAdjustments();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(sa.difference), 0) FROM StockAdjustment sa WHERE sa.product.id = :productId AND sa.status = 'COMPLETED'")
    Integer getTotalAdjustmentByProduct(@Param("productId") UUID productId);

    @Query("SELECT COALESCE(SUM(sa.difference), 0) FROM StockAdjustment sa WHERE sa.warehouse.id = :warehouseId AND sa.status = 'COMPLETED'")
    Integer getTotalAdjustmentByWarehouse(@Param("warehouseId") UUID warehouseId);

    @Query("SELECT COALESCE(SUM(sa.difference), 0) FROM StockAdjustment sa WHERE sa.product.id = :productId AND sa.warehouse.id = :warehouseId AND sa.status ='COMPLETED'")
    Integer getTotalAdjustmentByProductAndWarehouse(@Param("productId") UUID productId,
            @Param("warehouseId") UUID warehouseId);

    // // this is for report

    // @Query("SELECT sa.adjustmentType, COUNT(sa), COALESCE(SUM(sa.difference), 0)
    // FROM StockAdjustment sa WHERE sa.status = 'COMPLETED' GROUP BY
    // sa.adjustmentType")
    // List<Object[]> getAdjustmentSummaryByType();

    // @Query("SELECT sa.warehouse.name, COUNT(sa), COALESCE(SUM(sa.difference), 0)
    // FROM StockAdjustment sa WHERE sa.status = 'COMPLETED' GROUP BY
    // sa.warehouse.id, sa.warehouse.name")
    // List<Object[]> getAdjustmentSummaryByWarehouse();

    // List<StockAdjustment> findByCreatedBy(UUID createdById);

    // List<StockAdjustment> findByApprovedBy(UUID approvedById);

    @Query("SELECT sa FROM StockAdjustment sa JOIN FETCH sa.product WHERE sa.id=:adjustmentId")
    Optional<StockAdjustment> findWithProductById(@Param("adjustmentId") UUID adjustmentId);

    @Query("SELECT sa FROM StockAdjustment sa JOIN FETCH sa.product p JOIN FETCH sa.warehouse w LEFT JOIN FETCH sa.variant v WHERE sa.id=:adjustmentId")
    Optional<StockAdjustment> findWithAllDetailsById(@Param("adjustmentId") UUID adjustmentId);

    /**
     * Get all adjustments with product details for a warehouse
     */
    @Query("SELECT sa FROM StockAdjustment sa JOIN FETCH sa.product WHERE sa.warehouse.id = :warehouseId ORDER BY sa.createdAt DESC")
    List<StockAdjustment> findWithProductByWarehouseId(@Param("warehouseId") UUID warehouseId);

    @Query("SELECT sa FROM StockAdjustment sa JOIN FETCH sa.product ORDER BY sa.createdAt DESC")
    List<StockAdjustment> findAllWithProduct();

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.product.name LIKE CONCAT('%', :searchTerm, '%') OR sa.adjustmentNumber LIKE CONCAT('%',:searchTerm, '%')")

    List<StockAdjustment> searchAdjustments(@Param("searchTerm") String searchTerm);

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.reason LIKE CONCAT('%',:searchTerm,'%')")
    List<StockAdjustment> findByReasonContaining(
            @Param("searchTerm") String searchTerm);

}