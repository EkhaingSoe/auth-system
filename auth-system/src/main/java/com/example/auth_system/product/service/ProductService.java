package com.example.auth_system.product.service;

import com.example.auth_system.product.dto.request.CreateProductRequest;
import com.example.auth_system.product.dto.request.UpdateProductRequest;
import com.example.auth_system.product.dto.response.ProductResponse;
import com.example.auth_system.product.dto.response.ProductVariantResponse;
import com.example.auth_system.product.entity.ProductImage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    // Product CRUD
    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(UUID productId, UpdateProductRequest request);

    ProductResponse getProductById(UUID productId);

    ProductResponse getProductByCode(String productCode);

    ProductResponse getProductBySku(String sku);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getActiveProducts();

    List<ProductResponse> getProductsByCategory(UUID categoryId);

    List<ProductResponse> getProductsByBrand(UUID brandId);

    List<ProductResponse> searchProducts(String term);

    void deleteProduct(UUID productId);

    // Variant Operations
    ProductVariantResponse getVariantById(UUID variantId);

    ProductVariantResponse getVariantBySku(String sku);

    ProductResponse updateVariantStock(UUID variantId, Integer quantity);

    ProductResponse updateVariantReserved(UUID variantId, Integer quantity);

    List<ProductVariantResponse> getVariantsNeedingReorder();

    // Image Operations
    ProductResponse uploadProductImage(UUID productId, MultipartFile file, Boolean isPrimary);

    ProductResponse removeProductImage(UUID productId, UUID imageId);

    ProductResponse setPrimaryImage(UUID productId, UUID imageId);

    ProductResponse uploadVariantImage(UUID productId, UUID variantId, MultipartFile file, Boolean isPrimary);

    List<ProductImage> getProductImages(UUID productId);

    List<ProductImage> getVariantImages(UUID productId, UUID variantId);
}