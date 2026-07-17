package com.example.auth_system.order.order.dto.orderRequest;

import lombok.*;

import java.util.UUID;

import com.example.auth_system.order.order.enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    private OrderStatus orderStatus;

    private UUID updatedBy;
}