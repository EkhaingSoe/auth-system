package com.example.auth_system.inventory.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementSummaryResponse {
    private UUID productId;

    private String productName;

    private Integer totalIncoming;

    private Integer totalOutgoing;

    private Integer purchaseQuantity;

    private Integer saleQuantity;

    private Integer transferInQuantity;

    private Integer transferOutQuantity;

    private Integer adjustmentInQuantity;

    private Integer adjustmentOutQuantity;
}
