package com.example.auth_system.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStockResponse {

    private UUID id;

    private UUID productId;
    private String productName;

    private UUID variantId;
    private String variantSku;

    private UUID warehouseId;
    private String warehouseName;

    private Integer currentQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;

    private Integer minStock;
    private Integer maxStock;

    private Integer reorderLevel;
    private Integer reorderQuantity;

    private Boolean belowReorderLevel;
    private Boolean overMaxStock;

    private LocalDateTime lastUpdatedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
