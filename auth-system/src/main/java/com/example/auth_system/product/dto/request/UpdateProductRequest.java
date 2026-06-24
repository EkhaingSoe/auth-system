package com.example.auth_system.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.auth_system.product.dto.request.CreateProductRequest.SupplierRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;

    private String description;

    private UUID categoryId;

    private UUID brandId;

    private String productType;

    private Boolean saleOk;

    private Boolean posOk;

    private Boolean websiteOk;

    private BigDecimal taxRate;

    private Boolean isActive;

    @Valid
    private List<CreateProductRequest.CreateVariantRequest> variants;

    @Valid
    private List<SupplierRequest> suppliers;

    @Valid
    private List<CreateProductRequest.ImageRequest> images;
}