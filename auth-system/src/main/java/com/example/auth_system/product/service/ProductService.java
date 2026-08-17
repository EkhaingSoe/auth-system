package com.example.auth_system.product.service;

import com.example.auth_system.product.dto.request.CreateProductRequest;
import com.example.auth_system.product.dto.request.UpdateProductRequest;
import com.example.auth_system.product.dto.response.ProductResponse;
import com.example.auth_system.product.dto.response.ProductVariantResponse;
import com.example.auth_system.product.entity.ProductImage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    // Product CRUD
    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(UUID productId, UpdateProductRequest request);

    ProductResponse getProductById(UUID productId);

    ProductResponse getProductByCode(String productCode);

    ProductResponse getProductBySku(String sku);

    List<ProductResponse> getAllProducts();

    Page<ProductResponse> getActiveProducts(Pageable pageable);

    Page<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable);

    Page<ProductResponse> getProductsByBrand(UUID brandId, Pageable pageable);

    Page<ProductResponse> searchProducts(String term, Pageable pageable);

    void deleteProduct(UUID productId);

    // Variant Operations
    ProductVariantResponse getVariantById(UUID variantId);

    ProductVariantResponse getVariantBySku(String sku);

    ProductVariantResponse getVariantByBarcode(String barcode);

    // ProductResponse updateVariantStock(UUID variantId, Integer quantity);

    // ProductResponse updateVariantReserved(UUID variantId, Integer quantity);

    // List<ProductVariantResponse> getVariantsNeedingReorder();

    // Image Operations
    ProductResponse uploadProductImage(UUID productId, MultipartFile file, Boolean isPrimary);

    ProductResponse removeProductImage(UUID productId, UUID imageId);

    ProductResponse setPrimaryImage(UUID productId, UUID imageId);

    ProductResponse uploadVariantImage(UUID productId, UUID variantId, MultipartFile file, Boolean isPrimary);

    List<ProductImage> getProductImages(UUID productId);

    List<ProductImage> getVariantImages(UUID productId, UUID variantId);
}