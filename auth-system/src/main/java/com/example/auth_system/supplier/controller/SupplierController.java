package com.example.auth_system.supplier.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.supplier.dto.request.CreateSupplierRequest;
import com.example.auth_system.supplier.dto.request.UpdateSupplierRequest;
import com.example.auth_system.supplier.dto.response.SupplierResponse;
import com.example.auth_system.supplier.service.SupplierService;
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
@RequestMapping("/api/admin/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierController {

    private final SupplierService supplierService;

    // ============================================================
    // CRUD OPERATIONS
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('SUPPLIER_CREATE')")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @Valid @RequestBody CreateSupplierRequest request) {
        log.info("POST /api/admin/suppliers - Creating supplier: {}", request.getName());
        SupplierResponse supplier = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Supplier created successfully", supplier));
    }

    @GetMapping
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllSuppliers() {
        log.info("GET /api/admin/suppliers - Getting all suppliers");
        List<SupplierResponse> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success(200, "Suppliers retrieved successfully", suppliers));
    }

    @GetMapping("/active")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getActiveSuppliers() {
        log.info("GET /api/admin/suppliers/active - Getting active suppliers");
        List<SupplierResponse> suppliers = supplierService.getActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(200, "Active suppliers retrieved successfully", suppliers));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(@PathVariable UUID id) {
        log.info("GET /api/admin/suppliers/{} - Getting supplier by id", id);
        SupplierResponse supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Supplier retrieved successfully", supplier));
    }

    @GetMapping("/code/{supplierCode}")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierByCode(@PathVariable String supplierCode) {
        log.info("GET /api/admin/suppliers/code/{} - Getting supplier by code", supplierCode);
        SupplierResponse supplier = supplierService.getSupplierByCode(supplierCode);
        return ResponseEntity.ok(ApiResponse.success(200, "Supplier retrieved successfully", supplier));
    }

    @GetMapping("/search")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> searchSuppliers(@RequestParam String term) {
        log.info("GET /api/admin/suppliers/search - Searching suppliers with term: {}", term);
        List<SupplierResponse> suppliers = supplierService.searchSuppliers(term);
        return ResponseEntity.ok(ApiResponse.success(200, "Suppliers found", suppliers));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_UPDATE')")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSupplierRequest request) {
        log.info("PUT /api/admin/suppliers/{} - Updating supplier", id);
        SupplierResponse supplier = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Supplier updated successfully", supplier));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable UUID id) {
        log.info("DELETE /api/admin/suppliers/{} - Deleting supplier", id);
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Supplier deactivated successfully", null));
    }

    // ============================================================
    // ACTIVATE / DEACTIVATE
    // ============================================================

    @PatchMapping("/{id}/activate")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_UPDATE')")
    public ResponseEntity<ApiResponse<SupplierResponse>> activateSupplier(@PathVariable UUID id) {
        log.info("PATCH /api/admin/suppliers/{}/activate - Activating supplier", id);
        supplierService.activateSupplier(id);
        SupplierResponse supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Supplier activated successfully", supplier));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_UPDATE')")
    public ResponseEntity<ApiResponse<SupplierResponse>> deactivateSupplier(@PathVariable UUID id) {
        log.info("PATCH /api/admin/suppliers/{}/deactivate - Deactivating supplier", id);
        supplierService.deactivateSupplier(id);
        SupplierResponse supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Supplier deactivated successfully", supplier));
    }

    // ============================================================
    // STATISTICS
    // ============================================================

    @GetMapping("/count/active")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<Long>> countActiveSuppliers() {
        log.info("GET /api/admin/suppliers/count/active - Counting active suppliers");
        long count = supplierService.countActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(200, "Active supplier count retrieved", count));
    }

    // ============================================================
    // PRODUCT-SUPPLIER RELATIONSHIP
    // ============================================================

    @GetMapping("/product/{productId}")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getSuppliersByProductId(@PathVariable UUID productId) {
        log.info("GET /api/admin/suppliers/product/{} - Getting suppliers for product", productId);
        List<SupplierResponse> suppliers = supplierService.getSuppliersByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(200, "Suppliers for product retrieved", suppliers));
    }

    @GetMapping("/product/{productId}/primary")
    @PreAuthorize("@permission.hasPermission('SUPPLIER_READ')")
    public ResponseEntity<ApiResponse<SupplierResponse>> getPrimarySupplierByProductId(@PathVariable UUID productId) {
        log.info("GET /api/admin/suppliers/product/{}/primary - Getting primary supplier for product", productId);
        SupplierResponse supplier = supplierService.getPrimarySupplierByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(200, "Primary supplier retrieved", supplier));
    }
}