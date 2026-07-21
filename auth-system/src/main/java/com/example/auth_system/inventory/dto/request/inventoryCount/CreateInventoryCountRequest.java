package com.example.auth_system.inventory.dto.request.inventoryCount;

import com.example.auth_system.inventory.enums.CountType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInventoryCountRequest {

    @NotNull(message = "Warehouse ID is required")
    private UUID warehouseId;

    @NotNull(message = "Count type is required")
    private CountType countType;

    private LocalDateTime scheduledDate;

    private String notes;
}
