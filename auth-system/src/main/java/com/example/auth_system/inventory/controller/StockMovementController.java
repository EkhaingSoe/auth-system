package com.example.auth_system.inventory.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.inventory.dto.request.stockMovement.TransferStockRequest;
// import com.example.auth_system.inventory.dto.request.TransferStockRequest;
import com.example.auth_system.inventory.dto.response.StockMovementResponse;
import com.example.auth_system.inventory.dto.response.StockMovementSummaryResponse;
import com.example.auth_system.inventory.enums.MovementType;
import com.example.auth_system.inventory.service.StockMovementService;

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
@RequestMapping("/api/inventory/movements")
@RequiredArgsConstructor
@Slf4j
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getAllStockMovements() {

        log.info("GET /api/inventory/movements - Getting all stock movements");

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock movements retrieved",
                        stockMovementService.getAllStockMovements()));
    }

    @GetMapping("/{movementId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> getStockMovementById(
            @PathVariable UUID movementId) {

        log.info("GET /api/inventory/movements/{} - Getting stock movement",
                movementId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock movement retrieved",
                        stockMovementService.getStockMovementById(movementId)));
    }

    @GetMapping("/number/{movementNumber}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> getStockMovementByNumber(
            @PathVariable String movementNumber) {

        log.info("GET /api/inventory/movements/number/{} - Getting stock movement",
                movementNumber);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock movement retrieved",
                        stockMovementService.getStockMovementByNumber(movementNumber)));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getByProduct(
            @PathVariable UUID productId) {

        log.info("GET /api/inventory/movements/product/{} - Getting movements byproduct",
                productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Product stock movements retrieved",
                        stockMovementService.getStockMovementsByProduct(productId)));
    }

    @GetMapping("/variant/{variantId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getByVariant(
            @PathVariable UUID variantId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Variant stock movements retrieved",
                        stockMovementService.getStockMovementsByVariant(variantId)));
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getByWarehouse(
            @PathVariable UUID warehouseId) {

        log.info("GET /api/inventory/movements/warehouse/{} - Getting warehouse movements",
                warehouseId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Warehouse stock movements retrieved",
                        stockMovementService.getStockMovementsByWarehouse(warehouseId)));
    }

    // // ============================================================
    // // GET MOVEMENTS BY TYPE
    // // PURCHASE_IN
    // // SALE_OUT
    // // TRANSFER_IN
    // // TRANSFER_OUT
    // // ADJUSTMENT
    // // ============================================================

    @GetMapping("/type/{movementType}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getByMovementType(
            @PathVariable MovementType movementType) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock movements retrieved",
                        stockMovementService.getStockMovementsByType(movementType)));
    }

    // // ============================================================
    // // DATE RANGE SEARCH
    // // ============================================================

    @GetMapping("/date-range")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock movements retrieved",
                        stockMovementService.getStockMovementsByDateRange(start, end)));
    }

    // // ============================================================
    // // REFERENCE SEARCH
    // // Purchase ID / Sale Order ID / Adjustment ID
    // // ============================================================

    @GetMapping("/reference/{referenceId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getByReference(
            @PathVariable UUID referenceId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Reference stock movements retrieved",
                        stockMovementService.getStockMovementsByReference(referenceId)));
    }

    // // ============================================================
    // // TRANSFER STOCK
    // // ============================================================

    @PostMapping("/transfer")
    @PreAuthorize("@permission.hasPermission('INVENTORY_MANAGE')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> transferStock(
            @Valid @RequestBody TransferStockRequest request) {

        log.info("POST /api/inventory/movements/transfer - Transfer stock");

        StockMovementResponse response = stockMovementService.transferStock(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                201,
                                "Stock transferred successfully",
                                response));
    }

    // // ============================================================
    // // PRODUCT MOVEMENT SUMMARY
    // // ============================================================

    @GetMapping("/summary/product/{productId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<StockMovementSummaryResponse>> getMovementSummary(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock movement summary retrieved",
                        stockMovementService.getStockMovementSummary(productId)));
    }

    // // ============================================================
    // // TOTAL STOCK IN / OUT
    // // ============================================================

    @GetMapping("/total/product/{productId}")
    @PreAuthorize("@permission.hasPermission('INVENTORY_VIEW')")
    public ResponseEntity<ApiResponse<Object>> getTotalInOutQuantity(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Stock in out quantity retrieved",
                        stockMovementService.getTotalInOutQuantity(productId)));
    }

}