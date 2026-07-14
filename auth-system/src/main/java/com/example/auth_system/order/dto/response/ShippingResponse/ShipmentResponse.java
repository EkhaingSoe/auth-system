package com.example.auth_system.order.dto.response.ShippingResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.auth_system.order.enums.ShipmentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private UUID id;

    private UUID orderId;

    private String shippingMethod;

    private String trackingNumber;

    private String carrierName;

    private String carrierPhone;

    private LocalDateTime shippingDate;

    private LocalDate estimatedDelivery;

    private LocalDate actualDelivery;

    private BigDecimal shippingCost;

    private ShipmentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
