package com.example.auth_system.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDashboardResponse {
    private Integer totalProducts;

    private Integer lowStockProducts;

    private Integer outOfStockProducts;

    private Integer pendingAdjustments;

    private Integer pendingCounts;
}
