package com.example.auth_system.inventory.dto.request.stockAdjustment;

import com.example.auth_system.inventory.enums.AdjustmentType;

import jakarta.validation.constraints.Min;
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
public class UpdateStockAdjustmentRequest {

    @NotNull(message = "Product id is required")
    private UUID productId;

    private UUID variantId;

    @NotNull(message = "Warehouse id is required")
    private UUID warehouseId;

    @NotNull(message = "Adjustment type is required")
    private AdjustmentType adjustmentType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    private String reason;

    private String notes;
}
