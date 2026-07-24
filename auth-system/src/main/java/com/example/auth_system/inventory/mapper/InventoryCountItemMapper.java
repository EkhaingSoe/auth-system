package com.example.auth_system.inventory.mapper;

import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountItemResponse;
import com.example.auth_system.inventory.entity.InventoryCountItem;

public class InventoryCountItemMapper {

    public InventoryCountItemResponse toResponse(
            InventoryCountItem item) {

        return InventoryCountItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .variantId(
                        item.getVariant() != null
                                ? item.getVariant().getId()
                                : null)
                .systemQuantity(item.getSystemQuantity())
                .countedQuantity(item.getCountedQuantity())
                .difference(item.getDifference())
                .build();
    }

}
