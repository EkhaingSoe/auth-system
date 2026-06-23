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
public class ProductVariantResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String productCode;
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
    private List<ProductResponse.ProductImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}