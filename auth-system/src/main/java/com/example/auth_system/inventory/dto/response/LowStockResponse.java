package com.example.auth_system.inventory.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowStockResponse {
    private UUID productId;

    private String productName;

    private String warehouseName;

    private Integer availableQuantity;

    private Integer reorderLevel;
}
