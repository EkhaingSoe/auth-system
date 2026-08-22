package com.example.auth_system.inventory.repository;

import com.example.auth_system.inventory.entity.InventoryCount;
import com.example.auth_system.inventory.enums.CountType;
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

   @Query("SELECT ic FROM InventoryCount ic WHERE ic.warehouse.id = :warehouseId AND ic.status = 'PENDING'")
   List<InventoryCount> findPendingByWarehouseId(@Param("warehouseId") UUID warehouseId);

   @Query("SELECT ic FROM InventoryCount ic WHERE ic.warehouse.id = :warehouseId AND ic.status = 'IN_PROGRESS'")
   List<InventoryCount> findInProgressByWarehouseId(@Param("warehouseId") UUID warehouseId);

   long countByStatus(InventoryCountStatus status);

   long countByWarehouseId(UUID warehouseId);

   @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse WHERE ic.id= :countId")
   Optional<InventoryCount> findWithWarehouseById(@Param("countId") UUID countId);

   @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse w LEFT JOIN FETCH ic.createdBy u WHERE ic.id = :countId")
   Optional<InventoryCount> findWithAllDetailsById(@Param("countId") UUID countId);

   @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse ORDER BY ic.scheduledDate DESC")
   List<InventoryCount> findAllWithWarehouse();

   @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse WHERE ic.status = :status ORDER BY ic.scheduledDate DESC")
   List<InventoryCount> findByStatusWithWarehouse(@Param("status") InventoryCountStatus status);

   @Query("SELECT ic FROM InventoryCount ic JOIN FETCH ic.warehouse WHERE ic.status = 'PENDING' ORDER BY ic.scheduledDate ASC")
   List<InventoryCount> findPendingWithWarehouse();

   @Query("""
             SELECT ic
             FROM InventoryCount ic
             JOIN FETCH ic.warehouse
             WHERE LOWER(ic.countNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(ic.warehouse.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             ORDER BY ic.createdAt DESC
         """)
   List<InventoryCount> search(
         @Param("keyword") String keyword);

   @Query("""
             SELECT COUNT(ic) > 0
             FROM InventoryCount ic
             WHERE ic.warehouse.id = :warehouseId
               AND ic.countType = :countType
               AND ic.status <> :cancelledStatus
               AND ic.scheduledDate >= :startOfMonth
               AND ic.scheduledDate < :startOfNextMonth
         """)
   boolean existsCountForMonth(
         @Param("warehouseId") UUID warehouseId,
         @Param("countType") CountType countType,
         @Param("cancelledStatus") InventoryCountStatus cancelledStatus,
         @Param("startOfMonth") LocalDateTime startOfMonth,
         @Param("startOfNextMonth") LocalDateTime startOfNextMonth);

}