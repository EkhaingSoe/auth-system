package com.example.auth_system.order.dto.response.orderResponse;

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
public class OrderItemResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String productSku;

    private UUID variantId;
    private String variantSku;
    private JsonNode variantAttributes;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalPrice;

    private Integer refundedQuantity;
    private Boolean isRefunded;
}