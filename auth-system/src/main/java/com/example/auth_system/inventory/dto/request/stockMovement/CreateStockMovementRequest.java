package com.example.auth_system.inventory.dto.request.stockMovement;

import com.example.auth_system.inventory.enums.MovementType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class CreateStockMovementRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID variantId;

    @NotNull(message = "Movement type is required")
    private MovementType movementType;

    @NotNull(message = "Quantity is required")
    @Min(1)
    private Integer quantity;

    private UUID fromWarehouseId;
    private UUID toWarehouseId;

    @NotNull
    private BigDecimal unitCost;

    private UUID referenceId;
    private String referenceType;

    private String notes;
}