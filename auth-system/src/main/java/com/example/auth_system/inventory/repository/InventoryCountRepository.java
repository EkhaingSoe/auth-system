package com.example.auth_system.inventory.repository;

import com.example.auth_system.inventory.entity.InventoryCount;
import com.example.auth_system.inventory.enums.InventoryCountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryCountRepository extends JpaRepository<InventoryCount, UUID> {

    Optional<InventoryCount> findByCountNumber(String countNumber);

    List<InventoryCount> findByWarehouseId(UUID warehouseId);

    List<InventoryCount> findByStatus(InventoryCountStatus status);

    List<InventoryCount> findByWarehouseIdAndStatus(UUID warehouseId, InventoryCountStatus status);

    List<InventoryCount> findByCountDateBetween(LocalDateTime start, LocalDateTime end);

    List<InventoryCount> findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);

    List<InventoryCount> findByCompletedDateBetween(LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.scheduledDate >= :start AND ic.scheduledDate < :end")
    List<InventoryCount> findScheduledForToday();

    // /**
    // * Find inventory counts scheduled for a specific date
    // */
    @Query("SELECT ic FROM InventoryCount ic WHERE DATE(ic.scheduledDate) = :date")
    List<InventoryCount> findScheduledForDate(@Param("date") LocalDateTime date);

    // /**
    // * Find overdue inventory counts (scheduled date passed but not completed)
    // */
    // Scheduled:
    // July 20 10:00

    // Current:
    // July 21 10:00

    // Status:
    // PENDING
    @Query("SELECT ic FROM InventoryCount ic WHERE ic.scheduledDate < CURRENT_TIMESTAMP AND ic.status != 'COMPLETED' AND ic.status != 'CANCELLED'")
    List<InventoryCount> findOverdueCounts();

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.status = 'PENDING' ORDER BY ic.scheduledDate ASC")
    List<InventoryCount> findPendingCounts();

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.status = 'IN_PROGRESS'")
    List<InventoryCount> findInProgressCounts();

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.status = 'COMPLETED' ORDER BY ic.completedDate DESC")
    List<InventoryCount> findCompletedCounts();

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.status = 'VERIFIED'")
    List<InventoryCount> findVerifiedCounts();

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.warehouse.id = :warehouseId AND ic.status = 'PENDING'")
    List<InventoryCount> findPendingByWarehouseId(@Param("warehouseId") UUID warehouseId);

    @Query("SELECT ic FROM InventoryCount ic WHERE ic.warehouse.id = :warehouseId AND ic.status = 'IN_PROGRESS'")
    List<InventoryCount> findInProgressByWarehouseId(@Param("warehouseId") UUID warehouseId);

    long countByStatus(InventoryCountStatus status);

    long countByWarehouseId(UUID warehouseId);

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status = 'PENDING'")
    long countPendingCounts();

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status ='IN_PROGRESS'")
    long countInProgressCounts();

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status ='COMPLETED'")
    long countCompletedCounts();

    long countByCountDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.scheduledDate < CURRENT_TIMESTAMP AND ic.status != 'COMPLETED' AND ic.status != 'CANCELLED'")
    long countOverdueCounts();

    // // ============================================================
    // // SUMMARY / REPORT QUERIES
    // // ============================================================

    // /**
    // * Get inventory count summary by warehouse
    // */
    @Query("SELECT ic.warehouse.name, COUNT(ic), " +
            "SUM(CASE WHEN ic.status = 'COMPLETED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ic.status = 'IN_PROGRESS' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ic.status = 'PENDING' THEN 1 ELSE 0 END) " +
            "FROM InventoryCount ic GROUP BY ic.warehouse.id, ic.warehouse.name")
    List<Object[]> getCountSummaryByWarehouse();

    // /**
    // * Get inventory count statistics
    // */
    @Query("SELECT " +
            "COUNT(ic) as total, " +
            "SUM(CASE WHEN ic.status = 'PENDING' THEN 1 ELSE 0 END) as pending, " +
            "SUM(CASE WHEN ic.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgress, "
            +
            "SUM(CASE WHEN ic.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed, " +
            "SUM(CASE WHEN ic.status = 'VERIFIED' THEN 1 ELSE 0 END) as verified, " +
            "SUM(CASE WHEN ic.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled " +
            "FROM InventoryCount ic")
    Object[] getCountStatistics();

    // /**
    // * Get monthly count statistics
    // */
    @Query("SELECT FUNCTION('DATE_TRUNC', 'MONTH', ic.countDate) as month,COUNT(ic) " +
            "FROM InventoryCount ic WHERE ic.status = 'COMPLETED' " +
            "GROUP BY FUNCTION('DATE_TRUNC', 'MONTH', ic.countDate) " +
            "ORDER BY month DESC")
    List<Object[]> getMonthlyCountStatistics();

    // /**
    // * Get count completion rate
    // */
    @Query("SELECT "
            + "(SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status = 'COMPLETED' OR ic.status = 'VERIFIED') * 100.0 / "
            + "(SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status != 'CANCELLED') as completionRate")
    Double getCompletionRate();

    // // ============================================================
    // // JOIN FETCH QUERIES (For performance)
    // // ============================================================

    // /**
    // * Get inventory count with warehouse details
    // */
    @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse WHERE ic.id= :countId")
    Optional<InventoryCount> findWithWarehouseById(@Param("countId") UUID countId);

    // /**
    // * Get inventory count with warehouse and creator details
    // */
    @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse w LEFT JOIN FETCH ic.createdBy u WHERE ic.id = :countId")
    Optional<InventoryCount> findWithAllDetailsById(@Param("countId") UUID countId);

    // /**
    // * Get inventory counts with warehouse details
    // */
    @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse ORDER BY ic.scheduledDate DESC")
    List<InventoryCount> findAllWithWarehouse();

    // /**
    // * Get inventory counts with warehouse details by status
    // */
    @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse WHERE ic.status = :status ORDER BY ic.scheduledDate DESC")
    List<InventoryCount> findByStatusWithWarehouse(@Param("status") InventoryCountStatus status);

    // /**
    // * Get pending counts with warehouse details
    // */
    @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse WHERE ic.status = 'PENDING' ORDER BY ic.scheduledDate ASC")
    List<InventoryCount> findPendingWithWarehouse();

    // /**
    // * Search inventory counts by count number
    // */
    @Query("SELECT ic FROM InventoryCount ic WHERE ic.countNumber LIKE CONCAT('%', :searchTerm, '%')")
    List<InventoryCount> searchByCountNumber(@Param("searchTerm") String searchTerm);

    // /**
    // * Search inventory counts by warehouse name
    // */
    @Query("SELECT ic FROM InventoryCount ic WHERE ic.warehouse.name LIKE CONCAT('%', :searchTerm, '%')")
    List<InventoryCount> searchByWarehouseName(@Param("searchTerm") String searchTerm);

    // /**
    // * Search inventory counts by count number or warehouse name
    // */
    @Query("SELECT ic FROM InventoryCount ic WHERE ic.countNumber LIKE CONCAT('%', :searchTerm, '%') OR ic.warehouse.name LIKE CONCAT('%',:searchTerm, '%')")
    List<InventoryCount> searchCounts(@Param("searchTerm") String searchTerm);

    // /**
    // * Update counted items (JSON)
    // */
    // @Modifying
    // @Query("UPDATE InventoryCount ic SET ic.countedItems = :countedItems,
    // ic.updatedAt = CURRENT_TIMESTAMP WHERE ic.id = :countId")
    // int updateCountedItems(@Param("countId") UUID countId, @Param("countedItems")
    // String countedItems);

}