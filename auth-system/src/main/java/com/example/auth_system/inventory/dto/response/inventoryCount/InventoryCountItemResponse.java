package com.example.auth_system.inventory.dto.response.inventoryCount;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCountItemResponse {
    private UUID id;

    private UUID productId;

    private String productName;

    private String productCode;

    private UUID variantId;

    private String variantSku;

    private Integer systemQuantity;

    private Integer countedQuantity;

    private Integer difference;
}
