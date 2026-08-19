package com.example.auth_system.inventory.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.inventory.dto.response.LowStockResponse;
import com.example.auth_system.inventory.dto.response.StockSummaryResponse;
import com.example.auth_system.inventory.dto.response.WarehouseStockResponse;
import com.example.auth_system.inventory.service.WarehouseStockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/warehouse-stocks")
@RequiredArgsConstructor
@Slf4j
public class WarehouseStockController {

        private final WarehouseStockService warehouseStockService;

        @GetMapping
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<WarehouseStockResponse>>> getAllWarehouseStocks() {

                log.info("GET /api/admin/warehouse-stocks - Getting all warehouse stocks");

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Warehouse stocks retrieved successfully",
                                                warehouseStockService.getAllWarehouseStocks()));
        }

        @GetMapping("/{warehouseStockId}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<WarehouseStockResponse>> getWarehouseStockById(
                        @PathVariable UUID warehouseStockId) {

                log.info(
                                "GET /api/admin/warehouse-stocks/{} - Getting warehouse stock",
                                warehouseStockId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Warehouse stock retrieved successfully",
                                                warehouseStockService.getWarehouseStockById(warehouseStockId)));
        }

        @GetMapping("/warehouse/{warehouseId}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<WarehouseStockResponse>>> getStockByWarehouse(
                        @PathVariable UUID warehouseId) {

                log.info(
                                "GET /api/admin/warehouse-stocks/warehouse/{} - Getting warehouse stocks",
                                warehouseId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Warehouse stocks retrieved successfully",
                                                warehouseStockService.getStockByWarehouse(warehouseId)));
        }

        // /product/{productId}?variantId={blackVariantId}
        @GetMapping("/product/{productId}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<WarehouseStockResponse>>> getStockByProduct(
                        @PathVariable UUID productId,
                        @RequestParam(required = false) UUID variantId) {

                return ResponseEntity.ok(
                                ApiResponse.success(200,
                                                "Product stocks retrieved successfully",
                                                warehouseStockService.getStockByProduct(productId, variantId)));
        }

        @GetMapping("/summary")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<StockSummaryResponse>> getStockSummary(
                        @RequestParam UUID productId,
                        @RequestParam(required = false) UUID variantId) {

                return ResponseEntity.ok(ApiResponse.success(200,
                                "Stock summary retrieved successfully",
                                warehouseStockService.getStockSummary(productId, variantId)));
        }

        @GetMapping("/low-stock")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<LowStockResponse>>> getLowStockProducts() {

                log.info("GET /api/admin/warehouse-stocks/low-stock - Getting low stock products");

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Low stock products retrieved successfully",
                                                warehouseStockService.getLowStockProducts()));
        }

        @GetMapping("/out-of-stock")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<WarehouseStockResponse>>> getOutOfStockProducts() {

                log.info("GET /api/admin/warehouse-stocks/out-of-stock - Getting out of stock products");

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Out of stock products retrieved successfully",
                                                warehouseStockService.getOutOfStockProducts()));
        }

        @GetMapping("/over-stock")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<WarehouseStockResponse>>> getOverStockProducts() {

                log.info("GET /api/admin/warehouse-stocks/over-stock - Getting over stock products");

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Over stock products retrieved successfully",
                                                warehouseStockService.getOverStockProducts()));
        }
}