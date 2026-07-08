package com.example.auth_system.order.repository;

import com.example.auth_system.order.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, UUID> {

    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtDesc(UUID orderId);

    List<OrderStatusHistory> findByOrderId(UUID orderId);
}