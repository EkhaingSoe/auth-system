package com.example.auth_system.inventory.dto.request.stockAdjustment;

import com.example.auth_system.inventory.enums.AdjustmentType;
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
public class CreateStockAdjustmentRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID variantId;

    @NotNull(message = "Warehouse ID is required")
    private UUID warehouseId;

    @NotNull(message = "Adjustment type is required")
    private AdjustmentType adjustmentType;

    @NotNull(message = "Adjustment quantity is required")
    private Integer adjustmentQuantity; // +10 or -5

    @NotNull(message = "Reason is required")
    private String reason;

    private String notes;
}