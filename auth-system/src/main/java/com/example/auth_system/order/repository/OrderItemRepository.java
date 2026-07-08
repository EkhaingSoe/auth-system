package com.example.auth_system.order.repository;

import com.example.auth_system.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findByOrderId(UUID orderId);

    List<OrderItem> findByProductId(UUID productId);

    @Query("SELECT oi.productId, SUM(oi.quantity) as totalSold FROM OrderItem oi WHERE oi.order.orderStatus = 'DELIVERED' GROUP BY oi.productId ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();

    @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi WHERE oi.order.orderStatus = 'DELIVERED'")
    Double getTotalRevenueFromItems();
}