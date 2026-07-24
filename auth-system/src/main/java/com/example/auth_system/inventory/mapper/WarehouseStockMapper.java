package com.example.auth_system.inventory.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.auth_system.inventory.dto.response.LowStockResponse;
import com.example.auth_system.inventory.dto.response.StockSummaryResponse;
import com.example.auth_system.inventory.dto.response.WarehouseStockResponse;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.enums.StockStatus;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WarehouseStockMapper {

        public WarehouseStockResponse toWarehouseStockResponse(WarehouseStock warehouseStock) {

                if (warehouseStock == null) {
                        return null;
                }

                return WarehouseStockResponse.builder()
                                .warehouseId(warehouseStock.getWarehouse().getId())
                                .warehouseName(warehouseStock.getWarehouse().getName())
                                .currentQuantity(warehouseStock.getCurrentQuantity())
                                .reservedQuantity(warehouseStock.getReservedQuantity())
                                .availableQuantity(warehouseStock.getAvailableQuantity())
                                .minStock(warehouseStock.getMinStock())
                                .maxStock(warehouseStock.getMaxStock())
                                .reorderLevel(warehouseStock.getReorderLevel())
                                .needsReorder(warehouseStock.getAvailableQuantity() <= warehouseStock.getReorderLevel())
                                .build();
        }

        public StockSummaryResponse toStockSummaryResponse(
                        Product product,
                        ProductVariant variant,
                        List<WarehouseStock> warehouseStocks) {

                int totalStock = warehouseStocks.stream()
                                .mapToInt(WarehouseStock::getCurrentQuantity)
                                .sum();

                int reservedStock = warehouseStocks.stream()
                                .mapToInt(WarehouseStock::getReservedQuantity)
                                .sum();

                int availableStock = totalStock - reservedStock;

                int reorderLevel = warehouseStocks.stream()
                                .mapToInt(WarehouseStock::getReorderLevel)
                                .min()
                                .orElse(0);

                int minStock = warehouseStocks.stream()
                                .mapToInt(WarehouseStock::getMinStock)
                                .min()
                                .orElse(0);

                int maxStock = warehouseStocks.stream()
                                .mapToInt(WarehouseStock::getMaxStock)
                                .max()
                                .orElse(0);

                return StockSummaryResponse.builder()

                                .productId(product.getId())
                                .productName(product.getName())
                                .productCode(product.getProductCode())

                                .variantId(
                                                variant != null ? variant.getId() : null)
                                .variantSku(
                                                variant != null ? variant.getSku() : null)

                                .totalStock(totalStock)
                                .reservedStock(reservedStock)
                                .availableStock(availableStock)

                                .reorderLevel(reorderLevel)
                                .minStock(minStock)
                                .maxStock(maxStock)

                                .status(
                                                calculateStatus(
                                                                availableStock,
                                                                reorderLevel,
                                                                maxStock))

                                .warehouseStocks(
                                                warehouseStocks.stream()
                                                                .map(this::toWarehouseStockResponse)
                                                                .toList())

                                .build();
        }

        public LowStockResponse tLowStockResponse(WarehouseStock warehouseStock) {
                return LowStockResponse.builder()
                                .productId(warehouseStock.getProduct().getId())
                                .productName(warehouseStock.getProduct().getName())
                                .warehouseName(warehouseStock.getWarehouse().getName())
                                .availableQuantity(warehouseStock.getAvailableQuantity())
                                .reorderLevel(warehouseStock.getReorderLevel())
                                .build();
        }

        private StockStatus calculateStatus(
                        Integer availableStock,
                        Integer reorderLevel,
                        Integer maxStock) {

                if (availableStock <= 0) {
                        return StockStatus.OUT_OF_STOCK;
                }

                if (availableStock <= reorderLevel) {
                        return StockStatus.LOW_STOCK;
                }

                if (maxStock != null && availableStock > maxStock) {
                        return StockStatus.OVER_STOCK;
                }

                return StockStatus.IN_STOCK;
        }

}
