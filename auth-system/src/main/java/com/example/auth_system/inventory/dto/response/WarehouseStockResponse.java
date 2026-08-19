package com.example.auth_system.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.example.auth_system.inventory.enums.StockStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStockResponse {

    private UUID id;

    // Product
    private UUID productId;
    private String productName;

    // Variant
    private UUID variantId;
    // private String variantName;
    private String sku;

    // Warehouse
    private UUID warehouseId;
    private String warehouseName;

    // Stock quantities
    private Integer currentQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;

    // Stock configuration
    private Integer minStock;
    private Integer maxStock;
    private Integer reorderLevel;
    private Integer reorderQuantity;

    // Stock status
    private StockStatus status;
    private Boolean needsReorder;
}