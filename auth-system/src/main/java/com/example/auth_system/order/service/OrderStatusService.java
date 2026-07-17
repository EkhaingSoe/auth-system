package com.example.auth_system.order.service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.order.entity.Order;

public interface OrderStatusService {

    void changeStatus(
            Order order,
            OrderStatus newStatus,
            User user,
            String reason);
}
