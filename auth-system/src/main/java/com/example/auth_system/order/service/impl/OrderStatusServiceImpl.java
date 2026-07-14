package com.example.auth_system.order.service.impl;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.OrderStatusHistory;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.repository.OrderStatusHistoryRepository;
import com.example.auth_system.order.service.OrderStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusHistoryRepository statusHistoryRepository;

    @Override
    @Transactional
    public void changeStatus(
            Order order,
            OrderStatus newStatus,
            User user,
            String reason) {

        OrderStatus previousStatus = order.getOrderStatus();

        // no change
        if (previousStatus == newStatus) {
            return;
        }

        // update order status
        order.setOrderStatus(newStatus);

        // update timeline date
        switch (newStatus) {

            case PROCESSING ->
                order.setProcessingDate(LocalDateTime.now());

            case SHIPPED ->
                order.setShippedDate(LocalDateTime.now());

            case DELIVERED ->
                order.setDeliveredDate(LocalDateTime.now());

            case CANCELLED ->
                order.setCancelledDate(LocalDateTime.now());

            default -> {
            }
        }

        // create history
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(user)
                .reason(reason)
                .build();

        statusHistoryRepository.save(history);
    }
}
