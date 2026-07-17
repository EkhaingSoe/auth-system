package com.example.auth_system.order.repository;

import com.example.auth_system.order.enums.FulfillmentStatus;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.enums.PaymentStatus;
import com.example.auth_system.order.order.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByCustomerId(UUID customerId);

    List<Order> findByOrderStatus(OrderStatus status);

    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Order> findByFulfillmentStatus(FulfillmentStatus fulfillmentStatus);

    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.orderStatus IN :statuses")
    List<Order> findByCustomerIdAndStatuses(@Param("customerId") UUID customerId,
            @Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.orderNumber LIKE CONCAT('%', :searchTerm, '%') OR o.customer.email LIKE CONCAT('%', :searchTerm, '%')")
    List<Order> searchOrders(@Param("searchTerm") String searchTerm);

    long countByOrderStatus(OrderStatus status);

    @Query("SELECT SUM(o.grandTotal) FROM Order o WHERE o.orderStatus = 'DELIVERED'")
    BigDecimal getTotalRevenue();

    @Query("SELECT SUM(o.grandTotal) FROM Order o WHERE o.orderDate BETWEEN :start AND :end AND o.orderStatus = 'DELIVERED'")
    BigDecimal getRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
