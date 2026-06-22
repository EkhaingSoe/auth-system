// src/main/java/com/example/auth_system/brand/controller/BrandController.java
package com.example.auth_system.brand.controller;

import com.example.auth_system.brand.dto.request.CreateBrandRequest;
import com.example.auth_system.brand.dto.request.UpdateBrandRequest;
import com.example.auth_system.brand.dto.response.BrandResponse;
import com.example.auth_system.brand.service.BrandService;
import com.example.auth_system.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/brands")
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    private final BrandService brandService;

    // ============================================================
    // GET ENDPOINTS
    // ============================================================

    @GetMapping
    @PreAuthorize("@permission.hasPermission('BRAND_READ')")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {
        log.info("GET /api/admin/brands - Getting all brands");
        return ResponseEntity.ok(ApiResponse.success(200, "Brands retrieved", brandService.getAllBrands()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('BRAND_READ')")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(@PathVariable UUID id) {
        log.info("GET /api/admin/brands/{} - Getting brand by id", id);
        return ResponseEntity.ok(ApiResponse.success(200, "Brand retrieved", brandService.getBrandById(id)));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("@permission.hasPermission('BRAND_READ')")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandByName(@PathVariable String name) {
        log.info("GET /api/admin/brands/name/{} - Getting brand by name", name);
        return ResponseEntity.ok(ApiResponse.success(200, "Brand retrieved", brandService.getBrandByName(name)));
    }

    @GetMapping("/search")
    @PreAuthorize("@permission.hasPermission('BRAND_READ')")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> searchBrands(@RequestParam String term) {
        log.info("GET /api/admin/brands/search?term={} - Searching brands", term);
        return ResponseEntity.ok(ApiResponse.success(200, "Brands found", brandService.searchBrands(term)));
    }

    @GetMapping("/active")
    @PreAuthorize("@permission.hasPermission('BRAND_READ')")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getActiveBrands() {
        log.info("GET /api/admin/brands/active - Getting active brands");
        return ResponseEntity.ok(ApiResponse.success(200, "Active brands retrieved", brandService.getActiveBrands()));
    }

    // ============================================================
    // POST / PUT / DELETE ENDPOINTS
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('BRAND_CREATE')")
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(@Valid @RequestBody CreateBrandRequest request) {
        log.info("POST /api/admin/brands - Creating brand: {}", request.getName());
        BrandResponse brand = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Brand created successfully", brand));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('BRAND_UPDATE')")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBrandRequest request) {
        log.info("PUT /api/admin/brands/{} - Updating brand", id);
        BrandResponse brand = brandService.updateBrand(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Brand updated successfully", brand));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('BRAND_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable UUID id) {
        log.info("DELETE /api/admin/brands/{} - Deleting brand", id);
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Brand deleted successfully", null));
    }

    // ============================================================
    // IMAGE MANAGEMENT
    // ============================================================

    @PostMapping("/{brandId}/logo")
    @PreAuthorize("@permission.hasPermission('BRAND_UPDATE')")
    public ResponseEntity<ApiResponse<BrandResponse>> uploadBrandLogo(
            @PathVariable UUID brandId,
            @RequestParam("file") MultipartFile file) {

        log.info("POST /api/admin/brands/{}/logo - Uploading logo", brandId);

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        BrandResponse brand = brandService.uploadBrandLogo(brandId, file);
        return ResponseEntity.ok(ApiResponse.success(200, "Logo uploaded successfully", brand));
    }

    @DeleteMapping("/{brandId}/logo")
    @PreAuthorize("@permission.hasPermission('BRAND_UPDATE')")
    public ResponseEntity<ApiResponse<BrandResponse>> removeBrandLogo(@PathVariable UUID brandId) {
        log.info("DELETE /api/admin/brands/{}/logo - Removing logo", brandId);
        BrandResponse brand = brandService.removeBrandLogo(brandId);
        return ResponseEntity.ok(ApiResponse.success(200, "Logo removed successfully", brand));
    }
}