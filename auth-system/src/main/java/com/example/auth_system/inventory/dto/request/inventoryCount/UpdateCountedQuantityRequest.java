package com.example.auth_system.inventory.dto.request.inventoryCount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountedQuantityRequest {
    @NotNull(message = "Counted quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer countedQuantity;
}
