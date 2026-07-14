package com.example.auth_system.order.service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.enums.OrderStatus;

public interface OrderStatusService {

    void changeStatus(
            Order order,
            OrderStatus newStatus,
            User user,
            String reason);
}
