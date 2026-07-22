package com.example.auth_system.inventory.mapper;

import com.example.auth_system.inventory.dto.request.stockAdjustment.CreateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.dto.response.StockAdjustmentSummaryResponse;
import com.example.auth_system.inventory.entity.StockAdjustment;
import com.example.auth_system.inventory.repository.projection.StockAdjustmentSummaryProjection;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;

public class StockAdjustmentMapper {

    public StockAdjustment toEntity(CreateStockAdjustmentRequest request, Product product,
            ProductVariant productVariant, Store warehouse) {
        StockAdjustment stockAdjustment = StockAdjustment.builder()
                .product(product)
                .variant(productVariant)
                .warehouse(warehouse)
                .adjustmentType(request.getAdjustmentType())
                .reason(request.getReason())
                .build();
        return stockAdjustment;
    }

    public StockAdjustmentResponse toResponse(StockAdjustment stockAdjustment) {
        if (stockAdjustment == null) {
            return null;
        }
        return StockAdjustmentResponse.builder()
                .id(stockAdjustment.getId())
                .adjustmentNumber(stockAdjustment.getAdjustmentNumber())
                .productId(stockAdjustment.getProduct().getId())
                .productName(stockAdjustment.getProduct().getName())
                .productCode(stockAdjustment.getProduct().getProductCode())
                .variantId(stockAdjustment.getVariant() != null ? stockAdjustment.getVariant().getId() : null)
                .variantSku(stockAdjustment.getVariant() != null ? stockAdjustment.getVariant().getSku() : null)
                .variantAttributes(
                        stockAdjustment.getVariant() != null ? stockAdjustment.getVariant().getAttributeValues() : null)
                .warehouseId(stockAdjustment.getWarehouse().getId())
                .warehouseName(stockAdjustment.getWarehouse().getName())
                .adjustmentType(stockAdjustment.getAdjustmentType())
                .oldQuantity(stockAdjustment.getOldQuantity())
                .newQuantity(stockAdjustment.getNewQuantity())
                .difference(stockAdjustment.getDifference())
                .status(stockAdjustment.getStatus())
                .approvedBy(
                        stockAdjustment.getApprovedBy() != null
                                ? stockAdjustment.getApprovedBy().getId()
                                : null)
                .approvedByName(
                        stockAdjustment.getApprovedBy() != null
                                ? stockAdjustment.getApprovedBy().getUsername()
                                : null)
                .approvedAt(stockAdjustment.getApprovedAt())

                .createdBy(
                        stockAdjustment.getCreatedBy() != null
                                ? stockAdjustment.getCreatedBy().getId()
                                : null)
                .createdByName(
                        stockAdjustment.getCreatedBy() != null
                                ? stockAdjustment.getCreatedBy().getUsername()
                                : null)
                .createdAt(stockAdjustment.getCreatedAt())
                .updatedAt(stockAdjustment.getUpdatedAt())

                .reason(stockAdjustment.getReason())
                .build();
    }

    public StockAdjustmentSummaryResponse toSummaryResponse(
            StockAdjustmentSummaryProjection projection) {

        return StockAdjustmentSummaryResponse.builder()
                .warehouseId(projection.getWarehouseId())
                .warehouseName(projection.getWarehouseName())
                .adjustmentType(projection.getAdjustmentType())
                .totalAdjustments(
                        projection.getTotalAdjustments().intValue())
                .totalDifference(
                        projection.getTotalDifference().intValue())
                .positiveAdjustments(
                        projection.getPositiveAdjustments().intValue())
                .negativeAdjustments(
                        projection.getNegativeAdjustments().intValue())
                .build();
    }

}
