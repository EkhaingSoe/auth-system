package com.example.auth_system.inventory.dto.request.stockMovement;

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
public class TransferStockRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID variantId;

    @NotNull(message = "From warehouse ID is required")
    private UUID fromWarehouseId;

    @NotNull(message = "To warehouse ID is required")
    private UUID toWarehouseId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String notes;
    private UUID createdBy;
}