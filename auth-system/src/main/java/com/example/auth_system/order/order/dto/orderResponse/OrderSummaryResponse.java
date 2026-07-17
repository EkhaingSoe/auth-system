package com.example.auth_system.order.order.dto.orderResponse;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponse {

    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private BigDecimal grandTotal;

    private String currency;
    private BigDecimal exchangeRate;

    private String taxType;
    private BigDecimal taxRate;
}
