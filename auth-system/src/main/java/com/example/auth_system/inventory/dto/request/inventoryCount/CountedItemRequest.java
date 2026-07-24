package com.example.auth_system.inventory.dto.request.inventoryCount;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountedItemRequest {

    @NotNull(message = "Inventory count item ID is required")
    private UUID itemId;

    @NotNull(message = "Counted quantity is required")
    private Integer countedQuantity;
}