package com.example.auth_system.inventory.service;

import com.example.auth_system.inventory.dto.request.stockMovement.TransferStockRequest;
import com.example.auth_system.inventory.dto.response.StockInOutResponse;
// import com.example.auth_system.inventory.dto.request.TransferStockRequest;
import com.example.auth_system.inventory.dto.response.StockMovementResponse;
import com.example.auth_system.inventory.dto.response.StockMovementSummaryResponse;
import com.example.auth_system.inventory.entity.StockMovement;
import com.example.auth_system.inventory.enums.MovementType;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockMovementService {

    // ============================================================
    // Query APIs (Frontend)
    // ============================================================

    List<StockMovementResponse> getAllStockMovements();

    StockMovementResponse getStockMovementById(UUID movementId);

    StockMovementResponse getStockMovementByNumber(String movementNumber);

    List<StockMovementResponse> getStockMovementsByProduct(UUID productId);

    List<StockMovementResponse> getStockMovementsByVariant(UUID variantId);

    List<StockMovementResponse> getStockMovementsByWarehouse(UUID warehouseId);

    List<StockMovementResponse> getStockMovementsByType(MovementType movementType);

    List<StockMovementResponse> getStockMovementsByReference(UUID referenceId);

    List<StockMovementResponse> getStockMovementsByDateRange(
            LocalDateTime start,
            LocalDateTime end);

    // List<StockMovementResponse> getStockMovementsByProductAndDateRange(
    // UUID productId,
    // LocalDateTime start,
    // LocalDateTime end);

    StockMovementSummaryResponse getStockMovementSummary(UUID productId);

    StockInOutResponse getTotalInOutQuantity(UUID productId);

    // // ============================================================
    // // Inventory Operations
    // // ============================================================

    StockMovementResponse transferStock(TransferStockRequest request);

    // // ============================================================
    // // Internal APIs
    // // Called by Purchase, Sale, Adjustment, Inventory Count
    // // ============================================================

    // StockMovement createMovement(
    // MovementType movementType,
    // Product product,
    // ProductVariant variant,
    // Store warehouse,
    // Integer quantity,
    // UUID referenceId,
    // String remarks);

}