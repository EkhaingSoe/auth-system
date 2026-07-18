package com.example.auth_system.order.refund.dto.refundResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundItemResponse {

    private UUID id;

    private UUID orderItemId;

    private UUID productId;
    private String productName;
    private String productSku;

    private UUID variantId;
    private String variantSku;
    private JsonNode variantAttributes;

    private Integer refundQuantity;

    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;

    private BigDecimal refundAmount;

    private String refundReason;

    private LocalDateTime createdAt;
}
