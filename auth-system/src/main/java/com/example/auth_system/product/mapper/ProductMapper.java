package com.example.auth_system.product.mapper;

import com.example.auth_system.product.dto.request.CreateProductRequest;
import com.example.auth_system.product.dto.request.UpdateProductRequest;
import com.example.auth_system.product.dto.response.ProductResponse;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductImage;
import com.example.auth_system.product.entity.ProductSupplier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final ProductVariantMapper variantMapper;

    public ProductMapper(ProductVariantMapper variantMapper) {
        this.variantMapper = variantMapper;
    }

    public Product toEntity(CreateProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .productType(request.getProductType())
                .saleOk(request.getSaleOk())
                .posOk(request.getPosOk())
                .websiteOk(request.getWebsiteOk())
                .taxRate(request.getTaxRate())
                .isActive(true)
                .build();
    }

    public void updateEntity(Product product, UpdateProductRequest request) {
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getProductType() != null) {
            product.setProductType(request.getProductType());
        }
        if (request.getSaleOk() != null) {
            product.setSaleOk(request.getSaleOk());
        }
        if (request.getPosOk() != null) {
            product.setPosOk(request.getPosOk());
        }
        if (request.getWebsiteOk() != null) {
            product.setWebsiteOk(request.getWebsiteOk());
        }
        if (request.getTaxRate() != null) {
            product.setTaxRate(request.getTaxRate());
        }
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .productType(product.getProductType())
                .saleOk(product.getSaleOk())
                .posOk(product.getPosOk())
                .websiteOk(product.getWebsiteOk())
                .taxRate(product.getTaxRate())
                .isActive(product.getIsActive())
                .minPrice(product.getMinPrice())
                .maxPrice(product.getMaxPrice())
                .totalStock(product.getTotalStock())
                .variants(product.getVariants().stream()
                        .map(variantMapper::toResponse)
                        .collect(Collectors.toList()))
                .images(product.getImages().stream()
                        .map(this::toImageResponse)
                        .collect(Collectors.toList()))
                .suppliers(product.getSuppliers().stream()
                        .map(this::toSupplierResponse)
                        .collect(Collectors.toList()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public ProductResponse.ProductImageResponse toImageResponse(ProductImage image) {
        if (image == null) {
            return null;
        }
        return ProductResponse.ProductImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .publicId(image.getPublicId())
                .isPrimary(image.getIsPrimary())
                .altText(image.getAltText())
                .sortOrder(image.getSortOrder())
                .build();
    }

    public ProductResponse.ProductSupplierResponse toSupplierResponse(ProductSupplier supplier) {
        if (supplier == null) {
            return null;
        }
        return ProductResponse.ProductSupplierResponse.builder()
                .id(supplier.getId())
                .supplierId(supplier.getSupplier() != null ? supplier.getSupplier().getId() : null)
                .supplierName(supplier.getSupplier() != null ? supplier.getSupplier().getName() : null)
                .supplierCode(supplier.getSupplier() != null ? supplier.getSupplier().getSupplierCode() : null)
                .supplierProductCode(supplier.getSupplierProductCode())
                .supplierPrice(supplier.getSupplierPrice())
                .leadTimeDays(supplier.getLeadTimeDays())
                .isPrimary(supplier.getIsPrimary())
                .build();
    }
}