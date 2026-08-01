package com.example.auth_system.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountItemRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateCountedQuantityRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateInventoryCountItemsRequest;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountItemResponse;
import com.example.auth_system.inventory.service.InventoryCountItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory-count-items")
@RequiredArgsConstructor
public class InventoryCountItemController {

        private final InventoryCountItemService inventoryCountItemService;

        @PostMapping("/{inventoryCountId}")
        public ResponseEntity<ApiResponse<List<InventoryCountItemResponse>>> createInventoryCountItem(
                        @PathVariable UUID inventoryCountId,
                        @RequestBody @Valid List<CreateInventoryCountItemRequest> requests) {

                List<InventoryCountItemResponse> response = inventoryCountItemService.createInventoryCountitem(
                                inventoryCountId,
                                requests);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(
                                                201,
                                                "Inventory count items created successfully",
                                                response));
        }

        @GetMapping("/count/{inventoryCountId}")
        public ResponseEntity<ApiResponse<List<InventoryCountItemResponse>>> getByInventoryCount(
                        @PathVariable UUID inventoryCountId) {

                List<InventoryCountItemResponse> response = inventoryCountItemService
                                .getByInventoryCount(inventoryCountId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Inventory count items retrieved successfully",
                                                response));
        }

        @GetMapping("/count/{inventoryCountId}/discrepancies")
        public ResponseEntity<ApiResponse<List<InventoryCountItemResponse>>> getDiscrepancyItems(
                        @PathVariable UUID inventoryCountId) {

                List<InventoryCountItemResponse> response = inventoryCountItemService
                                .getDiscrepancyItems(inventoryCountId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Discrepancy items retrieved successfully",
                                                response));
        }

        @PutMapping("/{itemId}/count")
        public ResponseEntity<ApiResponse<InventoryCountItemResponse>> updateCountedQuantity(
                        @PathVariable UUID itemId,
                        @RequestBody @Valid UpdateCountedQuantityRequest request) {

                InventoryCountItemResponse response = inventoryCountItemService.countedQuantity(
                                itemId,
                                request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Counted quantity updated successfully",
                                                response));
        }
}