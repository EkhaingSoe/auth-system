package com.example.auth_system.inventory.mapper;

import com.example.auth_system.inventory.dto.response.StockMovementResponse;
import com.example.auth_system.inventory.entity.StockMovement;

public class StockMovementMapper {
    public StockMovementResponse toResponse(StockMovement movement) {
        if (movement == null) {
            return null;
        }
        return StockMovementResponse.builder()
                .id(movement.getId())
                .movementNumber(movement.getMovementNumber())
                .productId(
                        movement.getProduct().getId())
                .productName(
                        movement.getProduct().getName())
                .productCode(
                        movement.getProduct().getProductCode())

                .variantId(
                        movement.getVariant() != null
                                ? movement.getVariant().getId()
                                : null)

                .variantSku(movement.getVariant() != null ? movement.getVariant().getSku() : null)

                .movementType(
                        movement.getMovementType())

                .quantity(
                        movement.getQuantity())

                .fromWarehouseId(
                        movement.getFromWarehouse() != null
                                ? movement.getFromWarehouse().getId()
                                : null)

                .toWarehouseId(
                        movement.getToWarehouse() != null
                                ? movement.getToWarehouse().getId()
                                : null)

                .referenceId(
                        movement.getReferenceId())

                .referenceType(
                        movement.getReferenceType())

                .notes(
                        movement.getNotes())

                .createdAt(
                        movement.getCreatedAt())

                .build();
    }
}
