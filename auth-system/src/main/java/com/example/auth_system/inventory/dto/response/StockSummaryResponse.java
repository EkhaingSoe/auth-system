package com.example.auth_system.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.auth_system.inventory.enums.StockStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockSummaryResponse {

    private UUID productId;
    private String productName;
    private String productCode;
    private UUID variantId;
    private String variantSku;
    private Integer totalStock;
    private Integer reservedStock;
    private Integer availableStock;
    private BigDecimal averageCost;
    private Integer reorderLevel;
    private Integer minStock;
    private Integer maxStock;
    private List<WarehouseStockResponse> warehouseStocks;
    private StockStatus status;

    public StockStatus getStatus() {
        if (availableStock <= 0)
            return StockStatus.OUT_OF_STOCK;
        if (availableStock <= reorderLevel)
            return StockStatus.LOW_STOCK;
        if (maxStock != null
                && availableStock > maxStock)
            return StockStatus.OVER_STOCK;
        return StockStatus.IN_STOCK;
    }
}