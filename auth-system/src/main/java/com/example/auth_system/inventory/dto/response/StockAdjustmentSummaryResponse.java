package com.example.auth_system.inventory.dto.response;

import com.example.auth_system.inventory.enums.AdjustmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentSummaryResponse {

    private UUID warehouseId;
    private String warehouseName;
    private AdjustmentType adjustmentType;
    private Integer totalAdjustments;
    private Integer totalDifference;
    private Integer positiveAdjustments;
    private Integer negativeAdjustments;
}