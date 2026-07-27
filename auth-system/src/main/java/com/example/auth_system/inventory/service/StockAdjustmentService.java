package com.example.auth_system.inventory.service;

import com.example.auth_system.inventory.dto.request.stockAdjustment.ApproveStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.CreateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.UpdateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.dto.response.StockAdjustmentSummaryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockAdjustmentService {

    // Create / Update
    StockAdjustmentResponse createStockAdjustment(CreateStockAdjustmentRequest request);

    // StockAdjustmentResponse updateStockAdjustment(UUID adjustmentId,
    // UpdateStockAdjustmentRequest request);

    // // Get Methods
    // StockAdjustmentResponse getStockAdjustmentById(UUID adjustmentId);

    // StockAdjustmentResponse getStockAdjustmentByNumber(String adjustmentNumber);

    // List<StockAdjustmentResponse> getAllStockAdjustments();

    // List<StockAdjustmentResponse> getStockAdjustmentsByStatus(String status);

    // List<StockAdjustmentResponse> getPendingStockAdjustments();

    // List<StockAdjustmentResponse> getStockAdjustmentsByProduct(UUID productId);

    // List<StockAdjustmentResponse> getStockAdjustmentsByWarehouse(UUID
    // warehouseId);

    // List<StockAdjustmentResponse> getStockAdjustmentsByType(String
    // adjustmentType);

    // List<StockAdjustmentResponse> getStockAdjustmentsByDateRange(LocalDateTime
    // start, LocalDateTime end);

    // // Approval / Workflow
    // StockAdjustmentResponse approveStockAdjustment(UUID adjustmentId,
    // ApproveStockAdjustmentRequest request);

    // StockAdjustmentResponse rejectStockAdjustment(UUID adjustmentId, String
    // reason);

    // StockAdjustmentResponse completeStockAdjustment(UUID adjustmentId);

    // // Summary / Reports
    // List<StockAdjustmentSummaryResponse> getAdjustmentSummary();

    // List<StockAdjustmentSummaryResponse> getAdjustmentSummaryByWarehouse(UUID
    // warehouseId);

    // // Delete
    // void deleteStockAdjustment(UUID adjustmentId);
}
