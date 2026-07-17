package com.example.auth_system.order.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth_system.order.order.entity.OrderStatusHistory;

import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, UUID> {

    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtDesc(UUID orderId);

    List<OrderStatusHistory> findByOrderId(UUID orderId);
}