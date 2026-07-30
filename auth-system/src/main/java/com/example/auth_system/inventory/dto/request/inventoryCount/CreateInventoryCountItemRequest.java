package com.example.auth_system.inventory.dto.request.inventoryCount;

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
public class CreateInventoryCountItemRequest {

    @NotNull(message = "Product is required")
    private UUID productId;

    private UUID variantId;

    @NotNull(message = "Counted quantity is required")
    @Min(value = 0, message = "Counted quantity cannot be negative")
    private Integer countedQuantity;

    private String notes;
}
