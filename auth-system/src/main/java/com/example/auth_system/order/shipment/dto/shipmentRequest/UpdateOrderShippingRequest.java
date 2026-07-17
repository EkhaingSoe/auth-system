package com.example.auth_system.order.shipment.dto.shipmentRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.auth_system.order.shipment.enums.ShipmentStatus;

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

    private ShipmentStatus status;
}