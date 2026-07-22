package com.example.auth_system.inventory.repository.projection;

import java.util.UUID;

import com.example.auth_system.inventory.enums.AdjustmentType;

public interface StockAdjustmentSummaryProjection {
    UUID getWarehouseId();

    String getWarehouseName();

    AdjustmentType getAdjustmentType();

    Long getTotalAdjustments();

    Long getTotalDifference();

    Long getPositiveAdjustments();

    Long getNegativeAdjustments();
}
