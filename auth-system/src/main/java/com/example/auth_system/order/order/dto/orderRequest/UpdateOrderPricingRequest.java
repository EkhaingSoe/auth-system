package com.example.auth_system.order.order.dto.orderRequest;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderPricingRequest {

    private BigDecimal discountAmount;
    private BigDecimal shippingCost;

    private UUID approvedBy;
}