package com.example.auth_system.inventory.dto.response.inventoryCount;

import java.time.LocalDateTime;
import java.util.List;
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
public class InventoryCountDetailResponse {

    private UUID id;

    private String countNumber;

    private String warehouseName;

    private InventoryCountStatus status;

    private CountType countType;

    private LocalDateTime scheduledDate;

    private String notes;

    private List<InventoryCountItemResponse> items;
}
