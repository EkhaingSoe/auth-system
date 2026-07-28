package com.example.auth_system.inventory.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.inventory.dto.request.stockAdjustment.ApproveStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.CreateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.UpdateStockAdjustmentRequest;
// import com.example.auth_system.inventory.dto.request.ApproveStockAdjustmentRequest;
// import com.example.auth_system.inventory.dto.request.CreateStockAdjustmentRequest;
// import com.example.auth_system.inventory.dto.request.UpdateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.dto.response.StockAdjustmentSummaryResponse;
import com.example.auth_system.inventory.enums.AdjustmentStatus;
import com.example.auth_system.inventory.enums.AdjustmentType;
import com.example.auth_system.inventory.service.StockAdjustmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/adjustments")
@RequiredArgsConstructor
@Slf4j
public class StockAdjustmentController {

    private final StockAdjustmentService stockAdjustmentService;

    // ============================================================
    // CREATE STOCK ADJUSTMENT
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> createStockAdjustment(
            @Valid @RequestBody CreateStockAdjustmentRequest request) {
        log.info("POST /api/inventory/adjustments - Creating stock adjustment");
        StockAdjustmentResponse response = stockAdjustmentService.createStockAdjustment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Stock adjustment created successfully", response));
    }

    // ============================================================
    // UPDATE STOCK ADJUSTMENT
    // ============================================================

    @PatchMapping("/{adjustmentId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> updateStockAdjustment(
            @PathVariable UUID adjustmentId,
            @Valid @RequestBody UpdateStockAdjustmentRequest request) {
        log.info("PUT /api/inventory/adjustments/{} - Updating stock adjustment",
                adjustmentId);
        StockAdjustmentResponse response = stockAdjustmentService.updateStockAdjustment(adjustmentId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment updated successfully", response));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENT BY ID
    // // ============================================================

    @GetMapping("/{adjustmentId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> getStockAdjustmentById(
            @PathVariable UUID adjustmentId) {
        log.info("GET /api/inventory/adjustments/{} - Getting stock adjustment",
                adjustmentId);
        StockAdjustmentResponse response = stockAdjustmentService.getStockAdjustmentById(adjustmentId);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment retrieved", response));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENT BY NUMBER
    // // ============================================================

    @GetMapping("/number/{adjustmentNumber}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> getStockAdjustmentByNumber(
            @PathVariable String adjustmentNumber) {
        log.info("GET /api/inventory/adjustments/number/{} - Getting stock adjustment", adjustmentNumber);
        StockAdjustmentResponse response = stockAdjustmentService.getStockAdjustmentByNumber(adjustmentNumber);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment retrieved", response));
    }

    // // ============================================================
    // // GET ALL STOCK ADJUSTMENTS
    // // ============================================================

    @GetMapping
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getAllStockAdjustments() {
        log.info("GET /api/inventory/adjustments - Getting all stock adjustments");
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getAllStockAdjustments();
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENTS BY STATUS
    // // ============================================================

    @GetMapping("/status/{status}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getStockAdjustmentsByStatus(
            @PathVariable AdjustmentStatus status) {
        log.info("GET /api/inventory/adjustments/status/{} - Getting adjustments by status", status);
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getStockAdjustmentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // GET PENDING STOCK ADJUSTMENTS
    // // ============================================================

    @GetMapping("/pending")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getPendingStockAdjustments() {
        log.info("GET /api/inventory/adjustments/pending - Getting pending adjustments");
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getPendingStockAdjustments();
        return ResponseEntity.ok(ApiResponse.success(200, "Pending adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENTS BY PRODUCT
    // // ============================================================

    @GetMapping("/product/{productId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getStockAdjustmentsByProduct(
            @PathVariable UUID productId) {
        log.info("GET /api/inventory/adjustments/product/{} - Getting adjustments by product", productId);
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getStockAdjustmentsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENTS BY WAREHOUSE
    // // ============================================================

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getStockAdjustmentsByWarehouse(
            @PathVariable UUID warehouseId) {
        log.info("GET /api/inventory/adjustments/warehouse/{} - Getting adjustments by warehouse", warehouseId);
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getStockAdjustmentsByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENTS BY TYPE
    // // ============================================================

    @GetMapping("/type/{adjustmentType}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getStockAdjustmentsByType(
            @PathVariable AdjustmentType adjustmentType) {
        log.info("GET /api/inventory/adjustments/type/{} - Getting adjustments by type", adjustmentType);
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getStockAdjustmentsByType(adjustmentType);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // GET STOCK ADJUSTMENTS BY DATE RANGE
    // // ============================================================

    @GetMapping("/date-range")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockAdjustmentResponse>>> getStockAdjustmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("GET /api/inventory/adjustments/date-range - Getting adjustments between {} and {}", start, end);
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getStockAdjustmentsByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustments retrieved", adjustments));
    }

    // // ============================================================
    // // APPROVE STOCK ADJUSTMENT
    // // ============================================================

    @PatchMapping("/{adjustmentId}/approve")
    @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> approveStockAdjustment(
            @PathVariable UUID adjustmentId,
            @Valid @RequestBody ApproveStockAdjustmentRequest request) {
        log.info("PATCH /api/inventory/adjustments/{}/approve - Approving adjustment", adjustmentId);
        StockAdjustmentResponse response = stockAdjustmentService.approveStockAdjustment(adjustmentId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment approved successfully", response));
    }

    // // ============================================================
    // // REJECT STOCK ADJUSTMENT
    // // ============================================================

    @PatchMapping("/{adjustmentId}/reject")
    @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> rejectStockAdjustment(
            @PathVariable UUID adjustmentId,
            @RequestParam String reason) {
        log.info("PATCH /api/inventory/adjustments/{}/reject - Rejecting adjustment",
                adjustmentId);
        StockAdjustmentResponse response = stockAdjustmentService.rejectStockAdjustment(adjustmentId, reason);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment rejected successfully", response));
    }

    // // ============================================================
    // // COMPLETE STOCK ADJUSTMENT
    // // ============================================================

    @PatchMapping("/{adjustmentId}/complete")
    @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    public ResponseEntity<ApiResponse<StockAdjustmentResponse>> completeStockAdjustment(
            @PathVariable UUID adjustmentId) {
        log.info("PATCH /api/inventory/adjustments/{}/complete - Completing adjustment", adjustmentId);
        StockAdjustmentResponse response = stockAdjustmentService.completeStockAdjustment(adjustmentId);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment completed successfully", response));
    }

    // // ============================================================
    // // GET ADJUSTMENT SUMMARY
    // // ============================================================

    // @GetMapping("/summary")
    // @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    // public ResponseEntity<ApiResponse<List<StockAdjustmentSummaryResponse>>>
    // getAdjustmentSummary() {
    // log.info("GET /api/inventory/adjustments/summary - Getting adjustment
    // summary");
    // List<StockAdjustmentSummaryResponse> summary =
    // stockAdjustmentService.getAdjustmentSummary();
    // return ResponseEntity.ok(ApiResponse.success(200, "Adjustment summary
    // retrieved", summary));
    // }

    // @GetMapping("/summary/warehouse/{warehouseId}")
    // @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    // public ResponseEntity<ApiResponse<List<StockAdjustmentSummaryResponse>>>
    // getAdjustmentSummaryByWarehouse(
    // @PathVariable UUID warehouseId) {
    // log.info("GET /api/inventory/adjustments/summary/warehouse/{} - Getting
    // adjustment summary", warehouseId);
    // List<StockAdjustmentSummaryResponse> summary = stockAdjustmentService
    // .getAdjustmentSummaryByWarehouse(warehouseId);
    // return ResponseEntity.ok(ApiResponse.success(200, "Adjustment summary
    // retrieved", summary));
    // }

    // // ============================================================
    // // DELETE STOCK ADJUSTMENT
    // // ============================================================

    // @DeleteMapping("/{adjustmentId}")
    // @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    // public ResponseEntity<ApiResponse<Void>> deleteStockAdjustment(
    // @PathVariable UUID adjustmentId) {
    // log.info("DELETE /api/inventory/adjustments/{} - Deleting stock adjustment",
    // adjustmentId);
    // stockAdjustmentService.deleteStockAdjustment(adjustmentId);
    // return ResponseEntity.ok(ApiResponse.success(200, "Stock adjustment deleted
    // successfully", null));
    // }
}