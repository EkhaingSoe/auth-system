package com.example.auth_system.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateInventoryCountRequest;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountResponse;
import com.example.auth_system.inventory.enums.InventoryCountStatus;
import com.example.auth_system.inventory.service.InventoryCountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/inventory-counts")
@RequiredArgsConstructor
@Slf4j
public class InventoryCountController {

    private final InventoryCountService inventoryCountService;

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryCountResponse>> create(
            @RequestBody @Valid CreateInventoryCountRequest request) {

        InventoryCountResponse response = inventoryCountService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Inventory count created successfully", response));
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<ApiResponse<InventoryCountResponse>> update(
    // @PathVariable UUID id,
    // @RequestBody @Valid UpdateInventoryCountRequest request) {

    // InventoryCountResponse response = inventoryCountService.update(id, request);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory count updated successfully", response));
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

    // inventoryCountService.delete(id);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory count deleted successfully", null));
    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<ApiResponse<InventoryCountResponse>> getById(
    // @PathVariable UUID id) {

    // InventoryCountResponse response = inventoryCountService.getById(id);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory count retrieved successfully",
    // response));
    // }

    // @GetMapping
    // public ResponseEntity<ApiResponse<List<InventoryCountResponse>>> getAll() {

    // List<InventoryCountResponse> response = inventoryCountService.getAll();

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory counts retrieved successfully",
    // response));
    // }

    // @GetMapping("/search")
    // public ResponseEntity<ApiResponse<List<InventoryCountResponse>>> search(
    // @RequestParam String keyword) {

    // List<InventoryCountResponse> response =
    // inventoryCountService.search(keyword);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Search completed successfully", response));
    // }

    // @GetMapping("/warehouse/{warehouseId}")
    // public ResponseEntity<ApiResponse<List<InventoryCountResponse>>>
    // getByWarehouse(
    // @PathVariable UUID warehouseId) {

    // List<InventoryCountResponse> response =
    // inventoryCountService.getByWarehouse(warehouseId);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory counts retrieved successfully",
    // response));
    // }

    // @GetMapping("/status/{status}")
    // public ResponseEntity<ApiResponse<List<InventoryCountResponse>>> getByStatus(
    // @PathVariable InventoryCountStatus status) {

    // List<InventoryCountResponse> response =
    // inventoryCountService.getByStatus(status);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory counts retrieved successfully",
    // response));
    // }

    @PutMapping("/{id}/start")
    public ResponseEntity<ApiResponse<InventoryCountResponse>> startCount(
            @PathVariable UUID id) {

        InventoryCountResponse response = inventoryCountService.startCount(id);

        return ResponseEntity.ok(
                ApiResponse.success(200, "Inventory count started successfully", response));
    }

    // @PutMapping("/{id}/complete")
    // public ResponseEntity<ApiResponse<InventoryCountResponse>> completeCount(
    // @PathVariable UUID id) {

    // InventoryCountResponse response = inventoryCountService.completeCount(id);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory count completed successfully",
    // response));
    // }

    // @PutMapping("/{id}/verify")
    // public ResponseEntity<ApiResponse<InventoryCountResponse>> verifyCount(
    // @PathVariable UUID id) {

    // InventoryCountResponse response = inventoryCountService.verifyCount(id);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory count verified successfully", response));
    // }

    // @PutMapping("/{id}/cancel")
    // public ResponseEntity<ApiResponse<InventoryCountResponse>> cancelCount(
    // @PathVariable UUID id) {

    // InventoryCountResponse response = inventoryCountService.cancelCount(id);

    // return ResponseEntity.ok(
    // ApiResponse.success(200, "Inventory count cancelled successfully",
    // response));
    // }
}