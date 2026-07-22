package com.example.auth_system.inventory.dto.response.inventoryCount;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.auth_system.inventory.enums.CountType;
import com.example.auth_system.inventory.enums.InventoryCountStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCountResponse {

    private UUID id;

    private String countNumber;

    private UUID warehouseId;
    private String warehouseName;

    private CountType countType;

    private InventoryCountStatus status;

    private LocalDateTime scheduledDate;

    private LocalDateTime countDate;

    private LocalDateTime completedDate;

    private String createdByName;

    private String verifiedByName;

    private String notes;

    private Integer totalItems;

    private Integer countedItems;
}
