package com.example.auth_system.order.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.auth_system.order.order.entity.OrderShipment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderShipmentRepository extends JpaRepository<OrderShipment, UUID> {

    List<OrderShipment> findByOrderId(UUID orderId);

    Optional<OrderShipment> findByTrackingNumber(String trackingNumber);

    List<OrderShipment> findByCarrierName(String carrierName);

    List<OrderShipment> findByStatus(String status);

    List<OrderShipment> findByShippingMethod(String shippingMethod);

    List<OrderShipment> findByOrderIdAndStatus(UUID orderId, String status);

    // ============================================================
    // DATE-BASED QUERIES
    // ============================================================

    List<OrderShipment> findByShippingDateBetween(LocalDateTime start, LocalDateTime end);

    List<OrderShipment> findByEstimatedDelivery(LocalDate estimatedDelivery);

    List<OrderShipment> findByActualDelivery(LocalDate actualDelivery);

    List<OrderShipment> findByEstimatedDeliveryBefore(LocalDate date);

    List<OrderShipment> findByEstimatedDeliveryAfter(LocalDate date);

    @Query("SELECT os FROM OrderShipment os WHERE os.trackingNumber IS NULL OR os.trackingNumber = ''")
    List<OrderShipment> findShipmentsWithoutTracking();

    @Query("SELECT os FROM OrderShipment os WHERE os.actualDelivery IS NULL AND os.status != 'CANCELLED'")
    List<OrderShipment> findShipmentsNotDelivered();

    // ============================================================
    // STATUS-BASED QUERIES
    // ============================================================

    @Query("SELECT os FROM OrderShipment os WHERE os.status = 'PENDING' ORDER BY os.createdAt ASC")
    List<OrderShipment> findPendingShipments();

    /**
     * Find shipments that are overdue (estimated delivery passed, not delivered)
     */
    @Query("SELECT os FROM OrderShipment os WHERE os.estimatedDelivery < CURRENT_DATE AND os.status != 'DELIVERED' AND os.status != 'CANCELLED'")
    List<OrderShipment> findOverdueShipments();

    List<OrderShipment> findByCarrierNameAndStatus(String carrierName, String status);

    // ============================================================
    // COUNT METHODS
    // ============================================================

    long countByStatus(String status);

    long countByCarrierName(String carrierName);

    long countByShippingMethod(String shippingMethod);

    @Query("SELECT COUNT(os) FROM OrderShipment os WHERE os.actualDelivery BETWEEN :start AND :end")
    long countDeliveredBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // ============================================================
    // UPDATE METHODS
    // ============================================================
    @Modifying
    @Query("UPDATE OrderShipment os SET os.status = :status, os.updatedAt = CURRENT_TIMESTAMP WHERE os.order.id = :orderId")
    int updateStatusByOrderId(@Param("orderId") UUID orderId, @Param("status") String status);

    @Modifying
    @Query("UPDATE OrderShipment os SET os.trackingNumber = :trackingNumber, os.updatedAt = CURRENT_TIMESTAMP WHERE os.order.id = :orderId")
    int updateTrackingNumberByOrderId(@Param("orderId") UUID orderId, @Param("trackingNumber") String trackingNumber);

    @Modifying
    @Query("UPDATE OrderShipment os SET os.shippingDate = CURRENT_TIMESTAMP, os.status = 'SHIPPED', os.updatedAt = CURRENT_TIMESTAMP WHERE os.id = :shipmentId")
    int markAsShipped(@Param("shipmentId") UUID shipmentId);

    /**
     * Update actual delivery date and status when delivered
     */
    @Modifying
    @Query("UPDATE OrderShipment os SET os.actualDelivery = CURRENT_DATE, os.status = 'DELIVERED', os.updatedAt = CURRENT_TIMESTAMP WHERE os.id = :shipmentId")
    int markAsDelivered(@Param("shipmentId") UUID shipmentId);

    // ============================================================
    // SUMMARY / REPORT QUERIES
    // ============================================================

    @Query("SELECT SUM(os.shippingCost) FROM OrderShipment os WHERE os.order.id = :orderId")
    BigDecimal getTotalShippingCostByOrderId(@Param("orderId") UUID orderId);

    @Query("SELECT os.carrierName, SUM(os.shippingCost) FROM OrderShipment os GROUP BY os.carrierName")
    List<Object[]> getShippingCostByCarrier();

    @Query("SELECT COUNT(os) as total, " +
            "SUM(CASE WHEN os.actualDelivery <= os.estimatedDelivery THEN 1 ELSE 0 END) as onTime " +
            "FROM OrderShipment os WHERE os.status = 'DELIVERED'")
    Object[] getDeliveryPerformanceStats();

    @Query("SELECT os FROM OrderShipment os JOIN FETCH os.order WHERE os.order.id = :orderId")
    List<OrderShipment> findWithOrderByOrderId(@Param("orderId") UUID orderId);

    @Query("SELECT os FROM OrderShipment os JOIN FETCH os.order WHERE os.id = :shipmentId")
    Optional<OrderShipment> findWithOrderById(@Param("shipmentId") UUID shipmentId);

    // ============================================================
    // SEARCH METHODS
    // ============================================================

    @Query("SELECT os FROM OrderShipment os WHERE os.trackingNumber LIKE CONCAT('%', :searchTerm, '%') OR os.carrierName LIKE CONCAT('%', :searchTerm, '%')")
    List<OrderShipment> searchShipments(@Param("searchTerm") String searchTerm);

    @Query("SELECT os FROM OrderShipment os WHERE os.order.orderNumber LIKE CONCAT('%', :orderNumber, '%')")
    List<OrderShipment> findByOrderNumber(@Param("orderNumber") String orderNumber);
}