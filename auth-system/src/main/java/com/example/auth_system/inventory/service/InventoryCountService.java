package com.example.auth_system.inventory.service;

import java.util.List;
import java.util.UUID;

import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateInventoryCountRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountResponse;
import com.example.auth_system.inventory.enums.InventoryCountStatus;

public interface InventoryCountService {
    // CRUD
    InventoryCountResponse create(CreateInventoryCountRequest request);

    // InventoryCountResponse update(UUID countId, UpdateInventoryCountRequest
    // request);

    // void delete(UUID countId);

    // InventoryCountResponse getById(UUID countId);

    // List<InventoryCountResponse> getAll();

    // // Search
    // List<InventoryCountResponse> search(String keyword);

    // List<InventoryCountResponse> getByWarehouse(UUID warehouseId);

    // List<InventoryCountResponse> getByStatus(InventoryCountStatus status);

    // // Workflow
    InventoryCountResponse startCount(UUID countId);

    InventoryCountResponse completeCount(UUID countId);

    // InventoryCountResponse verifyCount(UUID countId);

    // InventoryCountResponse cancelCount(UUID countId);

    List<StockAdjustmentResponse> createAdjustment(UUID countId);
}
