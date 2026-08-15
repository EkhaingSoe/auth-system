package com.example.auth_system.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.auth_system.product.enums.ProductType;

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
    private ProductType productType;
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

}