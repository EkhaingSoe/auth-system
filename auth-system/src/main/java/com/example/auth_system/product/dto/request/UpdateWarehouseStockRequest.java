package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarehouseStockRequest {

    private UUID id;

    @NotNull(message = "Warehouse ID is required")
    private UUID warehouseId;

    private UUID variantId;

    private Integer currentQuantity;

    private Integer reservedQuantity;

    private Integer minStock;

    private Integer maxStock;

    private Integer reorderLevel;

    private Integer reorderQuantity;
}