package com.example.auth_system.order.service;

import com.example.auth_system.order.dto.request.CreateOrderRequest;
import com.example.auth_system.order.dto.request.UpdateOrderRequest;
import com.example.auth_system.order.dto.response.orderResponse.OrderResponse;
import com.example.auth_system.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    // CRUD Operations
    OrderResponse createOrder(CreateOrderRequest request);

    // OrderResponse updateOrder(UUID orderId, UpdateOrderRequest request);

    OrderResponse getOrderById(UUID orderId);

    OrderResponse getOrderByNumber(String orderNumber);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByCustomer(UUID customerId);

    List<OrderResponse> getOrdersByStatus(OrderStatus status);

    List<OrderResponse> getOrdersByDateRange(LocalDateTime start, LocalDateTime end);

    List<OrderResponse> searchOrders(String searchTerm);

    // // Status Management
    OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus, String reason);

    OrderResponse cancelOrder(UUID orderId, String reason);

    // // Statistics
    // long countOrdersByStatus(OrderStatus status);

    // BigDecimal getTotalRevenue();

    // BigDecimal getRevenueBetween(LocalDateTime start, LocalDateTime end);

    // // Delete
    // void deleteOrder(UUID orderId);
}