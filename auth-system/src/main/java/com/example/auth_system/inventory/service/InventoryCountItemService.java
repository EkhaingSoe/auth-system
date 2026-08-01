package com.example.auth_system.inventory.service;

import java.util.List;
import java.util.UUID;

import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountItemRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateCountedQuantityRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateInventoryCountItemsRequest;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountItemResponse;

public interface InventoryCountItemService {

    List<InventoryCountItemResponse> createInventoryCountitem(UUID inventoryCountId,
            List<CreateInventoryCountItemRequest> requests);

    List<InventoryCountItemResponse> getByInventoryCount(UUID inventoryCountId);

    List<InventoryCountItemResponse> getDiscrepancyItems(UUID inventoryCountId);

    InventoryCountItemResponse countedQuantity(UUID itemId, UpdateCountedQuantityRequest countedQuantity);

}
