// src/main/java/com/example/auth_system/store/controller/StoreController.java
package com.example.auth_system.store.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.store.dto.request.CreateStoreRequest;
import com.example.auth_system.store.dto.request.UpdateStoreRequest;
import com.example.auth_system.store.dto.response.StoreResponse;
import com.example.auth_system.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;

    // ============================================================
    // GET ENDPOINTS
    // ============================================================

    @GetMapping
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getAllStores() {
        log.info("GET /api/admin/stores - Getting all stores");
        return ResponseEntity.ok(ApiResponse.success(200, "Stores retrieved", storeService.getAllStores()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable UUID id) {
        log.info("GET /api/admin/stores/{} - Getting store by id", id);
        return ResponseEntity.ok(ApiResponse.success(200, "Store retrieved", storeService.getStoreById(id)));
    }

    @GetMapping("/code/{storeCode}")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreByCode(@PathVariable String storeCode) {
        log.info("GET /api/admin/stores/code/{} - Getting store by code", storeCode);
        return ResponseEntity.ok(ApiResponse.success(200, "Store retrieved", storeService.getStoreByCode(storeCode)));
    }

    @GetMapping("/search")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> searchStores(@RequestParam String term) {
        log.info("GET /api/admin/stores/search?term={} - Searching stores", term);
        return ResponseEntity.ok(ApiResponse.success(200, "Stores found", storeService.searchStores(term)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getStoresByStatus(@PathVariable String status) {
        log.info("GET /api/admin/stores/status/{} - Getting stores by status", status);
        return ResponseEntity.ok(ApiResponse.success(200, "Stores retrieved", storeService.getStoresByStatus(status)));
    }

    @GetMapping("/type/{storeType}")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getStoresByType(@PathVariable String storeType) {
        log.info("GET /api/admin/stores/type/{} - Getting stores by type", storeType);
        return ResponseEntity.ok(ApiResponse.success(200, "Stores retrieved", storeService.getStoresByType(storeType)));
    }

    @GetMapping("/head-offices")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getHeadOffices() {
        log.info("GET /api/admin/stores/head-offices - Getting head offices");
        return ResponseEntity.ok(ApiResponse.success(200, "Head offices retrieved", storeService.getHeadOffices()));
    }

    @GetMapping("/{parentId}/children")
    @PreAuthorize("@permission.hasPermission('STORE_READ')")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getChildStores(@PathVariable UUID parentId) {
        log.info("GET /api/admin/stores/{}/children - Getting child stores", parentId);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Child stores retrieved", storeService.getChildStores(parentId)));
    }

    // ============================================================
    // POST ENDPOINTS
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('STORE_CREATE')")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody CreateStoreRequest request) {
        log.info("POST /api/admin/stores - Creating store: {}", request.getName());
        StoreResponse store = storeService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Store created successfully", store));
    }

    // ============================================================
    // PUT ENDPOINTS
    // ============================================================

    @PutMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('STORE_UPDATE')")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStoreRequest request) {
        log.info("PUT /api/admin/stores/{} - Updating store", id);
        StoreResponse store = storeService.updateStore(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Store updated successfully", store));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("@permission.hasPermission('STORE_UPDATE')")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStoreStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        log.info("PATCH /api/admin/stores/{}/status - Updating status to {}", id, status);
        StoreResponse store = storeService.updateStoreStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(200, "Store status updated successfully", store));
    }

    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('STORE_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteStore(@PathVariable UUID id) {
        log.info("DELETE /api/admin/stores/{} - Deleting store", id);
        storeService.deleteStore(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Store deleted successfully", null));
    }

}