package com.example.auth_system.product.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.product.dto.request.CreateProductRequest;
import com.example.auth_system.product.dto.request.UpdateProductRequest;
import com.example.auth_system.product.dto.response.ProductResponse;
import com.example.auth_system.product.dto.response.ProductVariantResponse;
import com.example.auth_system.product.service.ProductService;
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
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    // ============================================================
    // PRODUCT CRUD
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('PRODUCT_CREATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        log.info("POST /api/admin/products - Creating product: {}", request.getName());
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Product created successfully", product));
    }

    @GetMapping
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        log.info("GET /api/admin/products - Getting all products");
        return ResponseEntity
                .ok(ApiResponse.success(200, "Products retrieved successfully", productService.getAllProducts()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable UUID id) {
        log.info("GET /api/admin/products/{} - Getting product by id", id);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Product retrieved successfully", productService.getProductById(id)));
    }

    @GetMapping("/code/{productCode}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductByCode(@PathVariable String productCode) {
        log.info("GET /api/admin/products/code/{} - Getting product by code", productCode);
        return ResponseEntity.ok(ApiResponse.success(200, "Product retrieved successfully",
                productService.getProductByCode(productCode)));
    }

    @GetMapping("/sku/{sku}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(@PathVariable String sku) {
        log.info("GET /api/admin/products/sku/{} - Getting product by sku", sku);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Product retrieved successfully", productService.getProductBySku(sku)));
    }

    @GetMapping("/search")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(@RequestParam String term) {
        log.info("GET /api/admin/products/search?term={} - Searching products", term);
        return ResponseEntity.ok(ApiResponse.success(200, "Products found", productService.searchProducts(term)));
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(@PathVariable UUID categoryId) {
        log.info("GET /api/admin/products/category/{} - Getting products by category", categoryId);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Products retrieved", productService.getProductsByCategory(categoryId)));
    }

    @GetMapping("/brand/{brandId}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByBrand(@PathVariable UUID brandId) {
        log.info("GET /api/admin/products/brand/{} - Getting products by brand", brandId);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Products retrieved", productService.getProductsByBrand(brandId)));
    }

    @GetMapping("/active")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        log.info("GET /api/admin/products/active - Getting active products");
        return ResponseEntity
                .ok(ApiResponse.success(200, "Active products retrieved", productService.getActiveProducts()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        log.info("PUT /api/admin/products/{} - Updating product", id);
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Product updated successfully", product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID id) {
        log.info("DELETE /api/admin/products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Product deleted successfully", null));
    }

    // ============================================================
    // VARIANT OPERATIONS
    // ============================================================

    @GetMapping("/variants/{variantId}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> getVariantById(@PathVariable UUID variantId) {
        log.info("GET /api/admin/products/variants/{} - Getting variant by id", variantId);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Variant retrieved", productService.getVariantById(variantId)));
    }

    @GetMapping("/variants/sku/{sku}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> getVariantBySku(@PathVariable String sku) {
        log.info("GET /api/admin/products/variants/sku/{} - Getting variant by sku", sku);
        return ResponseEntity.ok(ApiResponse.success(200, "Variant retrieved", productService.getVariantBySku(sku)));
    }

    @PatchMapping("/variants/{variantId}/stock")
    @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateVariantStock(
            @PathVariable UUID variantId,
            @RequestParam Integer quantity) {
        log.info("PATCH /api/admin/products/variants/{}/stock - Updating stock by: {}", variantId, quantity);
        ProductResponse product = productService.updateVariantStock(variantId, quantity);
        return ResponseEntity.ok(ApiResponse.success(200, "Stock updated successfully", product));
    }

    @PatchMapping("/variants/{variantId}/reserved")
    @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateVariantReserved(
            @PathVariable UUID variantId,
            @RequestParam Integer quantity) {
        log.info("PATCH /api/admin/products/variants/{}/reserved - Updating reserved by: {}", variantId, quantity);
        ProductResponse product = productService.updateVariantReserved(variantId, quantity);
        return ResponseEntity.ok(ApiResponse.success(200, "Reserved stock updated successfully", product));
    }

    @GetMapping("/variants/reorder")
    @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getVariantsNeedingReorder() {
        log.info("GET /api/admin/products/variants/reorder - Getting variants needing reorder");
        return ResponseEntity
                .ok(ApiResponse.success(200, "Variants needing reorder", productService.getVariantsNeedingReorder()));
    }

    // ============================================================
    // IMAGE OPERATIONS
    // ============================================================

    @PostMapping("/{productId}/images")
    @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> uploadProductImage(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary) {
        log.info("POST /api/admin/products/{}/images - Uploading image", productId);
        ProductResponse product = productService.uploadProductImage(productId, file, isPrimary);
        return ResponseEntity.ok(ApiResponse.success(200, "Image uploaded successfully", product));
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> removeProductImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        log.info("DELETE /api/admin/products/{}/images/{} - Removing image", productId, imageId);
        ProductResponse product = productService.removeProductImage(productId, imageId);
        return ResponseEntity.ok(ApiResponse.success(200, "Image removed successfully", product));
    }

    @PatchMapping("/{productId}/images/{imageId}/primary")
    @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> setPrimaryImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        log.info("PATCH /api/admin/products/{}/images/{}/primary - Setting primary image", productId, imageId);
        ProductResponse product = productService.setPrimaryImage(productId, imageId);
        return ResponseEntity.ok(ApiResponse.success(200, "Primary image set successfully", product));
    }
}