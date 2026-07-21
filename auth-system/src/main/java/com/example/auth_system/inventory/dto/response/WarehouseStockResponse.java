package com.example.auth_system.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStockResponse {

    private UUID warehouseId;
    private String warehouseName;
    private Integer currentQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer minStock;
    private Integer maxStock;
    private Integer reorderLevel;
    private Boolean needsReorder;
}