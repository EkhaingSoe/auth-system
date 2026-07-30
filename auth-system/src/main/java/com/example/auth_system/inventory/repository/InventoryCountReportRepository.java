package com.example.auth_system.inventory.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.auth_system.inventory.entity.InventoryCount;

public interface InventoryCountReportRepository extends JpaRepository<InventoryCount, UUID> {
    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status = 'PENDING'")
    long countPendingCounts();

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status ='IN_PROGRESS'")
    long countInProgressCounts();

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status ='COMPLETED'")
    long countCompletedCounts();

    long countByCountDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.scheduledDate < CURRENT_TIMESTAMP AND ic.status != 'COMPLETED' AND ic.status != 'CANCELLED'")
    long countOverdueCounts();

    @Query("SELECT ic.warehouse.name, COUNT(ic), " +
            "SUM(CASE WHEN ic.status = 'COMPLETED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ic.status = 'IN_PROGRESS' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ic.status = 'PENDING' THEN 1 ELSE 0 END) " +
            "FROM InventoryCount ic GROUP BY ic.warehouse.id, ic.warehouse.name")
    List<Object[]> getCountSummaryByWarehouse();

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

    @Query("SELECT FUNCTION('DATE_TRUNC', 'MONTH', ic.countDate) as month,COUNT(ic) " +
            "FROM InventoryCount ic WHERE ic.status = 'COMPLETED' " +
            "GROUP BY FUNCTION('DATE_TRUNC', 'MONTH', ic.countDate) " +
            "ORDER BY month DESC")
    List<Object[]> getMonthlyCountStatistics();

    @Query("SELECT "
            + "(SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status = 'COMPLETED' OR ic.status = 'VERIFIED') * 100.0 / "
            + "(SELECT COUNT(ic) FROM InventoryCount ic WHERE ic.status != 'CANCELLED') as completionRate")
    Double getCompletionRate();

}