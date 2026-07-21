package com.example.auth_system.inventory.dto.request.stockAdjustment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveStockAdjustmentRequest {

    private String notes;
}