package com.example.auth_system.order.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderShippingRequest {

    private String shippingMethod;

    private String trackingNumber;

    private String carrierName;

    private String carrierPhone;

    private BigDecimal shippingCost;

    private LocalDate estimatedDelivery;

    private LocalDate actualDelivery;

    private String status;
}