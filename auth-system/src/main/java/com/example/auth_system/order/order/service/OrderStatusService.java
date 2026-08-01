package com.example.auth_system.order.order.service;

import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.order.enums.OrderStatus;
import com.example.auth_system.user.entity.User;

public interface OrderStatusService {

    void changeStatus(
            Order order,
            OrderStatus newStatus,
            User user,
            String reason);
}
