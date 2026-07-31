package com.example.auth_system.inventory.service;

import java.util.List;
import java.util.UUID;

import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountItemRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateInventoryCountItemsRequest;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountItemResponse;

public interface InventoryCountItemService {

    List<InventoryCountItemResponse> createInventoryCountitem(UUID inventoryCountId,
            List<CreateInventoryCountItemRequest> requests);

    // InventoryCountItemResponse update(UUID itemId,
    // UpdateInventoryCountItemsRequest request);

    // void delete(UUID itemId);

    // InventoryCountItemResponse getById(UUID itemId);

    // List<InventoryCountItemResponse> getByInventoryCount(UUID inventoryCountId);

    // List<InventoryCountItemResponse> getDiscrepancyItems(UUID inventoryCountId);

    InventoryCountItemResponse CountedQuantity(UUID itemId, Integer countedQuantity);

    // List<InventoryCountItemResponse> createStockAdjustments(UUID
    // inventoryCountId);

}
