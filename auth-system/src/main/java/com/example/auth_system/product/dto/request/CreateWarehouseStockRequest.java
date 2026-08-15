package com.example.auth_system.product.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseStockRequest {

    @NotNull(message = "Warehouse ID is required")
    private UUID warehouseId;

    /**
     * Null when the product has no variants.
     * Non-null when stock belongs to a specific variant.
     */
    private UUID variantId;

    private Integer stockQuantity;

    private Integer reservedQuantity;

    private Integer minStock;

    private Integer maxStock;

    private Integer reorderLevel;

    private Integer reorderQuantity;
}
