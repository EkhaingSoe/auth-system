package com.example.auth_system.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private UUID id;
    private String productCode;
    private String name;
    private String description;
    private UUID categoryId;
    private String categoryName;
    private UUID brandId;
    private String brandName;
    private String productType;
    private Boolean saleOk;
    private Boolean posOk;
    private Boolean websiteOk;
    private BigDecimal taxRate;
    private Boolean isActive;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer totalStock;
    private List<ProductVariantResponse> variants;
    private List<ProductImageResponse> images;
    private List<ProductSupplierResponse> suppliers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductVariantResponse {
        private UUID id;
        private String sku;
        private String barcode;
        private BigDecimal sellingPrice;
        private BigDecimal costPrice;
        private String currency;
        private Integer stockQuantity;
        private Integer reservedQuantity;
        private Integer availableQuantity;
        private Integer minStockQuantity;
        private Integer maxStockQuantity;
        private Integer reorderLevel;
        private Object attributeValues;
        private BigDecimal weight;
        private BigDecimal length;
        private BigDecimal width;
        private BigDecimal height;
        private String unit;
        private Boolean isActive;
        private Boolean isInStock;
        private Boolean needsReorder;
        private List<ProductImageResponse> images;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageResponse {
        private UUID id;
        private String imageUrl;
        private String publicId;
        private Boolean isPrimary;
        private String altText;
        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSupplierResponse {
        private UUID id;
        private UUID supplierId;
        private String supplierName;
        private String supplierCode;
        private String supplierProductCode;
        private BigDecimal supplierPrice;
        private Integer leadTimeDays;
        private Boolean isPrimary;
    }
}