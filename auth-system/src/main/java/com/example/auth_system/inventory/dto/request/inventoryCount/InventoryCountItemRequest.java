package com.example.auth_system.inventory.dto.request.inventoryCount;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCountItemRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID variantId;

    @NotNull(message = "Counted quantity is required")
    private Integer countedQuantity;
}
