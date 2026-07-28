package com.example.auth_system.inventory.dto.request.stockAdjustment;

import com.example.auth_system.inventory.enums.AdjustmentDirection;
import com.example.auth_system.inventory.enums.AdjustmentType;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockAdjustmentRequest {

    private AdjustmentType adjustmentType;

    private AdjustmentDirection direction;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer newQuantity;

    private String reason;

}
