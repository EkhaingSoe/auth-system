package com.example.auth_system.inventory.controller;

import com.example.auth_system.inventory.dto.response.LowStockResponse;
import com.example.auth_system.inventory.dto.response.StockSummaryResponse;
import com.example.auth_system.inventory.dto.response.WarehouseStockResponse;
import com.example.auth_system.inventory.service.WarehouseStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/warehouse-stocks")
@RequiredArgsConstructor
public class WarehouseStockController {

    private final WarehouseStockService warehouseStockService;

    // @GetMapping
    // public ResponseEntity<List<WarehouseStockResponse>> getAllWarehouseStocks() {

    // return ResponseEntity.ok(
    // warehouseStockService.getAllWarehouseStocks());
    // }

    // @GetMapping("/{warehouseStockId}")
    // public ResponseEntity<WarehouseStockResponse> getWarehouseStockById(
    // @PathVariable UUID warehouseStockId) {

    // return ResponseEntity.ok(
    // warehouseStockService.getWarehouseStockById(warehouseStockId));
    // }

    // @GetMapping("/warehouse/{warehouseId}")
    // public ResponseEntity<List<WarehouseStockResponse>> getStockByWarehouse(
    // @PathVariable UUID warehouseId) {

    // return ResponseEntity.ok(
    // warehouseStockService.getStockByWarehouse(warehouseId));
    // }

    // @GetMapping("/product/{productId}")
    // public ResponseEntity<List<WarehouseStockResponse>> getStockByProduct(
    // @PathVariable UUID productId,
    // @RequestParam(required = false) UUID variantId) {

    // return ResponseEntity.ok(
    // warehouseStockService.getStockByProduct(productId, variantId));
    // }

    // @GetMapping("/summary")
    // public ResponseEntity<StockSummaryResponse> getStockSummary(
    // @RequestParam UUID productId,
    // @RequestParam(required = false) UUID variantId) {

    // return ResponseEntity.ok(
    // warehouseStockService.getStockSummary(productId, variantId));
    // }

    // @GetMapping("/low-stock")
    // public ResponseEntity<List<LowStockResponse>> getLowStockProducts() {

    // return ResponseEntity.ok(
    // warehouseStockService.getLowStockProducts());
    // }
}