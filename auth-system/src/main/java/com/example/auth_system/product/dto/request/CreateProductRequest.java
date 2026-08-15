package com.example.auth_system.product.dto.request;

import com.example.auth_system.product.enums.ProductType;
import com.example.auth_system.supplier.dto.request.CreateSupplierRequest;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;

    private String description;

    private UUID categoryId;

    private UUID brandId;

    private ProductType productType; // STOCKABLE, CONSUMABLE, SERVICE

    @Builder.Default
    private Boolean saleOk = true;

    @Builder.Default
    private Boolean posOk = true;

    @Builder.Default
    private Boolean websiteOk = true;

    private BigDecimal taxRate;

    @NotNull(message = "At least one variant is required")
    @Size(min = 1, message = "At least one variant is required")
    @Valid
    private List<CreateVariantRequest> variants;

    @NotNull(message = "At least one supplier is required")
    @Size(min = 1, message = "At least one supplier is required")
    @Valid
    private List<CreateProductSupplierRequest> suppliers;

    private List<ImageRequest> images;
    private List<VariantImageRequest> variantImages;
    private List<CreateWarehouseStockRequest> warehouseStocks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageRequest {
        @NotBlank(message = "Image URL is required")
        private String imageUrl;

        private Boolean isPrimary;

        private String altText;

        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantImageRequest {
        private UUID variantId; // Which variant this image belongs to
        private String imageUrl;
        private Boolean isPrimary;
        private String altText;
        private Integer sortOrder;
    }
}