package com.example.auth_system.inventory.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountRequest;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountDetailResponse;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountResponse;
import com.example.auth_system.inventory.entity.InventoryCount;
import com.example.auth_system.store.entity.Store;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryCountMapper {
    private final InventoryCountItemMapper inventoryCountItemMapper;

    public InventoryCount toEntity(CreateInventoryCountRequest request, Store warehouse, User createdBy) {
        InventoryCount inventoryCount = InventoryCount.builder()
                .warehouse(warehouse)
                .countType(request.getCountType())
                .scheduledDate(request.getScheduledDate())
                .notes(request.getNotes())
                .createdBy(createdBy)
                .build();

        return inventoryCount;
    }

    public InventoryCountResponse toInventoryCountResponse(InventoryCount inventoryCount) {
        int totalItems = inventoryCount.getItems().size();
        int countedItems = (int) inventoryCount.getItems()
                .stream()
                .filter(item -> item.getCountedQuantity() != null)
                .count();
        return InventoryCountResponse.builder()
                .id(inventoryCount.getId())
                .countNumber(inventoryCount.getCountNumber())
                .warehouseId(inventoryCount.getWarehouse().getId())
                .warehouseName(inventoryCount.getWarehouse().getName())
                .countType(inventoryCount.getCountType())
                .status(inventoryCount.getStatus())
                .scheduledDate(inventoryCount.getScheduledDate())
                .countDate(inventoryCount.getCountDate())
                .completedDate(inventoryCount.getCompletedDate())
                .createdByName(inventoryCount.getCreatedBy().getUsername())
                .verifiedByName(inventoryCount.getVerifiedBy().getUsername())
                .notes(inventoryCount.getNotes())
                .totalItems(totalItems)
                .countedItems(countedItems)
                .build();
    }

    public InventoryCountDetailResponse toDetailResponse(InventoryCount inventoryCount) {

        return InventoryCountDetailResponse.builder()
                .id(inventoryCount.getId())
                .countNumber(inventoryCount.getCountNumber())
                .warehouseName(inventoryCount.getWarehouse().getName())
                .status(inventoryCount.getStatus())
                .countType(inventoryCount.getCountType())
                .scheduledDate(inventoryCount.getScheduledDate())
                .notes(inventoryCount.getNotes())
                .items(inventoryCount.getItems()
                        .stream()
                        .map(inventoryCountItemMapper::toResponse)
                        .toList())
                .build();
    }
}
