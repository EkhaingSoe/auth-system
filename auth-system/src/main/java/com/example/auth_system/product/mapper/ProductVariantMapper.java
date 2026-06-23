package com.example.auth_system.product.mapper;

import com.example.auth_system.product.dto.request.CreateProductRequest;
import com.example.auth_system.product.dto.response.ProductResponse;
import com.example.auth_system.product.dto.response.ProductVariantResponse;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariant toEntity(CreateProductRequest.CreateVariantRequest request, Product product) {
        return ProductVariant.builder()
                .product(product)
                .sku(request.getSku())
                .barcode(request.getBarcode())
                .sellingPrice(request.getSellingPrice())
                .costPrice(request.getCostPrice())
                .currency(request.getCurrency() != null ? request.getCurrency() : "MMK")
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0)
                .reservedQuantity(request.getReservedQuantity() != null ? request.getReservedQuantity() : 0)
                .minStockQuantity(request.getMinStockQuantity() != null ? request.getMinStockQuantity() : 0)
                .maxStockQuantity(request.getMaxStockQuantity() != null ? request.getMaxStockQuantity() : 0)
                .reorderLevel(request.getReorderLevel() != null ? request.getReorderLevel() : 0)
                .attributeValues(request.getAttributeValues())
                .weight(request.getWeight())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .unit(request.getUnit() != null ? request.getUnit() : "piece")
                .isActive(true)
                .build();
    }

    public ProductResponse.ProductVariantResponse toResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        return ProductResponse.ProductVariantResponse.builder()
                .id(variant.getId())
                .sku(variant.getSku())
                .barcode(variant.getBarcode())
                .sellingPrice(variant.getSellingPrice())
                .costPrice(variant.getCostPrice())
                .currency(variant.getCurrency())
                .stockQuantity(variant.getStockQuantity())
                .reservedQuantity(variant.getReservedQuantity())
                .availableQuantity(variant.getAvailableQuantity())
                .minStockQuantity(variant.getMinStockQuantity())
                .maxStockQuantity(variant.getMaxStockQuantity())
                .reorderLevel(variant.getReorderLevel())
                .attributeValues(variant.getAttributeValues())
                .weight(variant.getWeight())
                .length(variant.getLength())
                .width(variant.getWidth())
                .height(variant.getHeight())
                .unit(variant.getUnit())
                .isActive(variant.getIsActive())
                .isInStock(variant.isInStock())
                .needsReorder(variant.needsReorder())
                .images(variant.getImages().stream()
                        .map(image -> ProductResponse.ProductImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .publicId(image.getPublicId())
                                .isPrimary(image.getIsPrimary())
                                .altText(image.getAltText())
                                .sortOrder(image.getSortOrder())
                                .build())
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }

    public ProductVariantResponse toFullResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        return ProductVariantResponse.builder()
                .id(variant.getId())
                .productId(variant.getProduct() != null ? variant.getProduct().getId() : null)
                .productName(variant.getProduct() != null ? variant.getProduct().getName() : null)
                .productCode(variant.getProduct() != null ? variant.getProduct().getProductCode() : null)
                .sku(variant.getSku())
                .barcode(variant.getBarcode())
                .sellingPrice(variant.getSellingPrice())
                .costPrice(variant.getCostPrice())
                .currency(variant.getCurrency())
                .stockQuantity(variant.getStockQuantity())
                .reservedQuantity(variant.getReservedQuantity())
                .availableQuantity(variant.getAvailableQuantity())
                .minStockQuantity(variant.getMinStockQuantity())
                .maxStockQuantity(variant.getMaxStockQuantity())
                .reorderLevel(variant.getReorderLevel())
                .attributeValues(variant.getAttributeValues())
                .weight(variant.getWeight())
                .length(variant.getLength())
                .width(variant.getWidth())
                .height(variant.getHeight())
                .unit(variant.getUnit())
                .isActive(variant.getIsActive())
                .isInStock(variant.isInStock())
                .needsReorder(variant.needsReorder())
                .images(variant.getImages().stream()
                        .map(image -> ProductResponse.ProductImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .publicId(image.getPublicId())
                                .isPrimary(image.getIsPrimary())
                                .altText(image.getAltText())
                                .sortOrder(image.getSortOrder())
                                .build())
                        .collect(java.util.stream.Collectors.toList()))
                .createdAt(variant.getCreatedAt())
                .updatedAt(variant.getUpdatedAt())
                .build();
    }
}