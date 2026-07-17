package com.example.auth_system.order.order.dto.orderRequest;

import com.example.auth_system.order.enums.OrderStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    private OrderStatus orderStatus;

    private UUID updatedBy;
}