package com.example.auth_system.product.service.impl;

import com.example.auth_system.brand.entity.Brand;
import com.example.auth_system.brand.repository.BrandRepository;
import com.example.auth_system.category.entity.Category;
import com.example.auth_system.category.repository.CategoryRepository;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CloudinaryService;
import com.example.auth_system.product.dto.request.CreateProductRequest;
import com.example.auth_system.product.dto.request.UpdateProductRequest;
import com.example.auth_system.product.dto.response.ProductResponse;
import com.example.auth_system.product.dto.response.ProductVariantResponse;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductImage;
import com.example.auth_system.product.entity.ProductSupplier;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.product.entity.ProductWarehouse;
import com.example.auth_system.product.mapper.ProductMapper;
import com.example.auth_system.product.mapper.ProductVariantMapper;
import com.example.auth_system.product.repository.*;
import com.example.auth_system.product.service.ProductService;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.repository.StoreRepository;
import com.example.auth_system.supplier.entity.Supplier;
import com.example.auth_system.supplier.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository imageRepository;
    private final ProductSupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository2;
    private final ProductMapper productMapper;
    private final ProductVariantMapper variantMapper;
    private final CloudinaryService cloudinaryService;
    private final StoreRepository storeRepository;  // ← Add this
    private final ProductWarehouseRepository productWarehouseRepository;

    private static final String PRODUCT_CODE_PREFIX = "PRD-";
    private static final int PRODUCT_CODE_PADDING = 4;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating product: {}", request.getName());

        // Validate category // Find category by ID (returns Category entity)
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + request.getCategoryId()));
        }

        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));
        }

        // Check for duplicate SKUs
        request.getVariants().forEach(variant -> {
            if (variantRepository.existsBySku(variant.getSku())) {
                throw new BusinessException("Variant with SKU already exists: " + variant.getSku());
            }
            if (variant.getBarcode() != null && variantRepository.existsByBarcode(variant.getBarcode())) {
                throw new BusinessException("Variant with barcode already exists: " + variant.getBarcode());
            }
        });

        // Create product
        Product product = productMapper.toEntity(request);
        product.setCategory(category); // Set the Category entity into Product // product.category = Category(id="cat-001", name="Clothing", ...)
        product.setBrand(brand);

        // Generate product code
        String productCode = generateProductCode();
        product.setProductCode(productCode);

        // Save product first
        product = productRepository.save(product);

        // Create variants
        for (CreateProductRequest.CreateVariantRequest variantRequest : request.getVariants()) {
            ProductVariant variant = variantMapper.toEntity(variantRequest, product);
            variant = variantRepository.save(variant);
            product.addVariant(variant);
        }

        // Create suppliers
        if (request.getSuppliers() != null) {
            for (CreateProductRequest.SupplierRequest supplierRequest : request.getSuppliers()) {
                Supplier supplier = supplierRepository2.findById(supplierRequest.getSupplierId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Supplier not found with id: " + supplierRequest.getSupplierId()));

                ProductSupplier productSupplier = ProductSupplier.builder()
                        .product(product)
                        .supplier(supplier)
                        .supplierProductCode(supplierRequest.getSupplierProductCode())
                        .supplierPrice(supplierRequest.getSupplierPrice())
                        .leadTimeDays(supplierRequest.getLeadTimeDays() != null ? supplierRequest.getLeadTimeDays() : 7)
                        .isPrimary(supplierRequest.getIsPrimary())
                        .build();

                supplierRepository.save(productSupplier);
                product.addSupplier(productSupplier);
            }
        }

        if (request.getWarehouseStocks() != null && !request.getWarehouseStocks().isEmpty()) {
            for (CreateProductRequest.WarehouseStockRequest stockRequest : request.getWarehouseStocks()) {
                // Validate store exists
                Store store = storeRepository.findById(stockRequest.getWarehouseId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Store not found with id: " + stockRequest.getWarehouseId()));

                // Check if store is active
                if (!"ACTIVE".equals(store.getStatus())) {
                    throw new BusinessException("Store is not active: " + store.getName());
                }

                // Create product-warehouse link
                ProductWarehouse productWarehouse = ProductWarehouse.builder()
                        .product(product)
                        .warehouse(store)
                        .stockQuantity(stockRequest.getStockQuantity() != null ? stockRequest.getStockQuantity() : 0)
                        .reservedQuantity(stockRequest.getReservedQuantity() != null ? stockRequest.getReservedQuantity() : 0)
                        .minStock(stockRequest.getMinStock() != null ? stockRequest.getMinStock() : 0)
                        .maxStock(stockRequest.getMaxStock() != null ? stockRequest.getMaxStock() : 0)
                        .build();

                productWarehouseRepository.save(productWarehouse);
                // product.addWarehouseStock(productWarehouse); // If you have this method
            }
            log.info("Created {} warehouse stocks for product", request.getWarehouseStocks().size());
        }

        // Create images
        if (request.getImages() != null) {
            for (CreateProductRequest.ImageRequest imageRequest : request.getImages()) {
                ProductImage image = ProductImage.builder()
                        .product(product)
                        .imageUrl(imageRequest.getImageUrl())
                        .isPrimary(imageRequest.getIsPrimary() != null ? imageRequest.getIsPrimary() : false)
                        .altText(imageRequest.getAltText())
                        .sortOrder(imageRequest.getSortOrder() != null ? imageRequest.getSortOrder() : 0)
                        .build();

                imageRepository.save(image);
                product.addImage(image);
            }
        }

        log.info("Product created successfully with ID: {} and code: {}", product.getId(), product.getProductCode());
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(UUID productId, UpdateProductRequest request) {
        log.info("Updating product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Update category
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }

        // Update brand
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));
            product.setBrand(brand);
        }

        // Update basic fields
        productMapper.updateEntity(product, request);

        // Update variants (if provided)
        if (request.getVariants() != null) {
            // Clear existing variants
            product.getVariants().clear();

            // Create new variants
            for (CreateProductRequest.CreateVariantRequest variantRequest : request.getVariants()) {
                // Check for duplicate SKU
                if (variantRepository.existsBySku(variantRequest.getSku())) {
                    throw new BusinessException("Variant with SKU already exists: " + variantRequest.getSku());
                }

                ProductVariant variant = variantMapper.toEntity(variantRequest, product);
                variant = variantRepository.save(variant);
                product.addVariant(variant);
            }
        }

        // Update suppliers (if provided)
        if (request.getSuppliers() != null) {
            // Clear existing suppliers
            product.getSuppliers().clear();

            // Create new suppliers
            for (CreateProductRequest.SupplierRequest supplierRequest : request.getSuppliers()) {
                Supplier supplier = supplierRepository2.findById(supplierRequest.getSupplierId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Supplier not found with id: " + supplierRequest.getSupplierId()));

                ProductSupplier productSupplier = ProductSupplier.builder()
                        .product(product)
                        .supplier(supplier)
                        .supplierProductCode(supplierRequest.getSupplierProductCode())
                        .supplierPrice(supplierRequest.getSupplierPrice())
                        .leadTimeDays(supplierRequest.getLeadTimeDays() != null ? supplierRequest.getLeadTimeDays() : 7)
                        .isPrimary(supplierRequest.getIsPrimary())
                        .build();

                supplierRepository.save(productSupplier);
                product.addSupplier(productSupplier);
            }
        }

        // Update images (if provided)
        if (request.getImages() != null) {
            // Clear existing images
            product.getImages().clear();

            // Create new images
            for (CreateProductRequest.ImageRequest imageRequest : request.getImages()) {
                ProductImage image = ProductImage.builder()
                        .product(product)
                        .imageUrl(imageRequest.getImageUrl())
                        .isPrimary(imageRequest.getIsPrimary() != null ? imageRequest.getIsPrimary() : false)
                        .altText(imageRequest.getAltText())
                        .sortOrder(imageRequest.getSortOrder() != null ? imageRequest.getSortOrder() : 0)
                        .build();

                imageRepository.save(image);
                product.addImage(image);
            }
        }

        if (request.getWarehouseStocks() != null) {
        // Clear existing warehouse stocks
        List<ProductWarehouse> existingStocks = productWarehouseRepository.findByProductId(product.getId());
        if (!existingStocks.isEmpty()) {
            productWarehouseRepository.deleteAll(existingStocks);
        }

        // Create new warehouse stocks
        for (CreateProductRequest.WarehouseStockRequest stockRequest : request.getWarehouseStocks()) {
            Store store = storeRepository.findById(stockRequest.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Store not found with id: " + stockRequest.getWarehouseId()));

            ProductWarehouse productWarehouse = ProductWarehouse.builder()
                    .product(product)
                    .warehouse(store)
                    .stockQuantity(stockRequest.getStockQuantity() != null ? stockRequest.getStockQuantity() : 0)
                    .reservedQuantity(stockRequest.getReservedQuantity() != null ? stockRequest.getReservedQuantity() : 0)
                    .minStock(stockRequest.getMinStock() != null ? stockRequest.getMinStock() : 0)
                    .maxStock(stockRequest.getMaxStock() != null ? stockRequest.getMaxStock() : 0)
                    .build();

            productWarehouseRepository.save(productWarehouse);
        }
    }

        product = productRepository.save(product);
        log.info("Product updated successfully: {}", productId);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID productId) {
        log.info("Getting product by id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByCode(String productCode) {
        log.info("Getting product by code: {}", productCode);
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + productCode));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySku(String sku) {
        log.info("Getting product by SKU: {}", sku);
        Product product = productRepository.findByVariantSku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        log.info("Getting active products");
        return productRepository.findByIsActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(UUID categoryId) {
        log.info("Getting products by category: {}", categoryId);
        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByBrand(UUID brandId) {
        log.info("Getting products by brand: {}", brandId);
        return productRepository.findByBrandId(brandId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String term) {
        log.info("Searching products with term: {}", term);
        return productRepository.searchProducts(term).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(UUID productId) {
        log.info("Deleting product: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Soft delete - just deactivate
        product.setIsActive(false);
        productRepository.save(product);
        log.info("Product deactivated successfully: {}", productId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getVariantById(UUID variantId) {
        log.info("Getting variant by id: {}", variantId);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));
        return variantMapper.toFullResponse(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getVariantBySku(String sku) {
        log.info("Getting variant by SKU: {}", sku);
        ProductVariant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with SKU: " + sku));
        return variantMapper.toFullResponse(variant);
    }

    @Override
    public ProductResponse updateVariantStock(UUID variantId, Integer quantity) {
        log.info("Updating variant stock: {} by {}", variantId, quantity);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));

        variant.setStockQuantity(variant.getStockQuantity() + quantity);
        variant = variantRepository.save(variant);

        return productMapper.toResponse(variant.getProduct());
    }

    @Override
    public ProductResponse updateVariantReserved(UUID variantId, Integer quantity) {
        log.info("Updating variant reserved: {} by {}", variantId, quantity);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));

        variant.setReservedQuantity(variant.getReservedQuantity() + quantity);
        variant = variantRepository.save(variant);

        return productMapper.toResponse(variant.getProduct());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariantsNeedingReorder() {
        log.info("Getting variants needing reorder");
        return variantRepository.findVariantsNeedingReorder().stream()
                .map(variantMapper::toFullResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse uploadProductImage(UUID productId, MultipartFile file, Boolean isPrimary) {
        log.info("Uploading image for product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        try {
            // Upload to Cloudinary
            var uploadResult = cloudinaryService.uploadImage(file, "products");

            String imageUrl = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            // If this is primary, clear existing primary images
            if (isPrimary) {
                imageRepository.clearPrimaryImages(productId);
            }

            ProductImage image = ProductImage.builder()
                    .product(product)
                    .variant(null)
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .isPrimary(isPrimary)
                    .altText(file.getOriginalFilename())
                    .sortOrder(product.getImages().size())
                    .isActive(true)
                    .build();

            imageRepository.save(image);
            product.addImage(image);

            log.info("Image uploaded successfully for product: {}", productId);
            return productMapper.toResponse(product);
        } catch (Exception e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new BusinessException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse uploadVariantImage(UUID productId, UUID variantId, MultipartFile file, Boolean isPrimary) {
        log.info("Uploading image for variant: {} of product: {}", variantId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));

        if (!variant.getProduct().getId().equals(productId)) {
            throw new BusinessException("Variant does not belong to this product");
        }

        try {

            var uploadResult = cloudinaryService.uploadImage(file, "products/" + productId + "/variants/" + variantId);

            String imageUrl = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            // If this is primary, clear existing primary images for this variant
            if (isPrimary) {
                imageRepository.clearPrimaryVariantImages(variantId);
            }

            ProductImage image = ProductImage.builder()
                    .product(null) // ← NULL for variant-level images
                    .variant(variant)
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .isPrimary(isPrimary)
                    .altText(file.getOriginalFilename())
                    .sortOrder(variant.getImages().size())
                    .build();

            imageRepository.save(image);
            variant.addImage(image);

            log.info("Variant image uploaded successfully for variant: {}", variantId);
            return productMapper.toResponse(product);
        } catch (Exception e) {
            log.error("Failed to upload variant image: {}", e.getMessage());
            throw new BusinessException("Failed to upload variant image: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse removeProductImage(UUID productId, UUID imageId) {
        log.info("Removing image: {} from product: {}", imageId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        // Check if image belongs to this product (either at product or variant level)
        boolean belongsToProduct = false;
        if (image.getProduct() != null && image.getProduct().getId().equals(productId)) {
            belongsToProduct = true;
        } else if (image.getVariant() != null && image.getVariant().getProduct().getId().equals(productId)) {
            belongsToProduct = true;
        }

        if (!belongsToProduct) {
            throw new BusinessException("Image does not belong to this product");
        }

        // Delete from Cloudinary
        try {
            if (image.getPublicId() != null) {
                cloudinaryService.deleteImage(image.getPublicId());
            }
        } catch (Exception e) {
            log.warn("Failed to delete image from Cloudinary: {}", e.getMessage());
        }

        // Remove from appropriate parent
        if (image.getProduct() != null) {
            image.getProduct().getImages().remove(image);
        } else if (image.getVariant() != null) {
            image.getVariant().getImages().remove(image);
        }

        imageRepository.delete(image);

        // If deleted image was primary, set another as primary
        if (image.getIsPrimary()) {
            if (image.getProduct() != null) {
                // Product-level: set first available as primary
                List<ProductImage> remainingImages = imageRepository.findByProductId(productId);
                if (!remainingImages.isEmpty()) {
                    ProductImage firstImage = remainingImages.get(0);
                    firstImage.setIsPrimary(true);
                    imageRepository.save(firstImage);
                }
            } else if (image.getVariant() != null) {
                // Variant-level: set first available as primary
                List<ProductImage> remainingImages = imageRepository.findByVariantId(image.getVariant().getId());
                if (!remainingImages.isEmpty()) {
                    ProductImage firstImage = remainingImages.get(0);
                    firstImage.setIsPrimary(true);
                    imageRepository.save(firstImage);
                }
            }
        }

        log.info("Image removed successfully: {}", imageId);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse setPrimaryImage(UUID productId, UUID imageId) {
        log.info("Setting primary image: {} for product: {}", imageId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        boolean belongsToProduct = false;
        UUID variantId = null;

        if (image.getProduct() != null && image.getProduct().getId().equals(productId)) {
            belongsToProduct = true;
        } else if (image.getVariant() != null && image.getVariant().getProduct().getId().equals(productId)) {
            belongsToProduct = true;
            variantId = image.getVariant().getId();
        }

        if (!belongsToProduct) {
            throw new BusinessException("Image does not belong to this product");
        }

        // Clear existing primary for the same level (product or variant)
        if (image.getProduct() != null) {
            imageRepository.clearPrimaryImages(productId);
        } else if (image.getVariant() != null) {
            imageRepository.clearPrimaryVariantImages(variantId);
        }

        // Set new primary
        image.setIsPrimary(true);
        imageRepository.save(image);

        log.info("Primary image set successfully: {}", imageId);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImage> getProductImages(UUID productId) {
        log.info("Getting product images for product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Get only product-level images (variant_id IS NULL)
        return imageRepository.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImage> getVariantImages(UUID productId, UUID variantId) {
        log.info("Getting variant images for variant: {} of product: {}", variantId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));

        if (!variant.getProduct().getId().equals(productId)) {
            throw new BusinessException("Variant does not belong to this product");
        }

        return imageRepository.findByVariantId(variantId);
    }

    // @Override
    // @Transactional(readOnly = true)
    // public List<ProductImage> getAllImagesForProduct(UUID productId) {
    // log.info("Getting all images (product + variants) for product: {}",
    // productId);

    // // Verify product exists
    // Product product = productRepository.findById(productId)
    // .orElseThrow(() -> new ResourceNotFoundException("Product not found with id:
    // " + productId));

    // // Get all images: product-level + all variant-level
    // return imageRepository.findByProductIdOrVariantId(productId);
    // }

    private String generateProductCode() {
        long count = productRepository.count() + 1;
        return PRODUCT_CODE_PREFIX + String.format("%0" + PRODUCT_CODE_PADDING + "d", count);
    }
}