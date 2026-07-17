package com.example.auth_system.order.order.service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.order.enums.OrderStatus;

public interface OrderStatusService {

    void changeStatus(
            Order order,
            OrderStatus newStatus,
            User user,
            String reason);
}
