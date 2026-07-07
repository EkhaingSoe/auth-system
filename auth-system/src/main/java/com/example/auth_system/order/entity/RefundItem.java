package com.example.auth_system.order.entity;

import com.fasterxml.jackson.databind.JsonNode;
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
public class RefundItem {

    private UUID orderItemId;

    private UUID productId;

    private UUID variantId;

    private String productName;

    private String productSku;

    private String variantSku;

    private JsonNode variantAttributes;

    private Integer refundQuantity;

    private BigDecimal unitPrice;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    private BigDecimal refundAmount;

    private String refundReason;
}