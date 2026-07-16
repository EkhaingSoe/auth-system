package com.example.auth_system.order.dto.request.Order;

import com.example.auth_system.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {

    private OrderStatus orderStatus;
    private String shippingTrackingNumber;
    private String shippingMethod;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;

    private BigDecimal discountAmount;
    private BigDecimal shippingCost;

    private String internalNotes;
    private UUID approvedBy;
}