package com.example.auth_system.inventory.dto.response;

import com.example.auth_system.inventory.enums.MovementType;
import com.example.auth_system.inventory.enums.ReferenceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponse {

    private UUID id;
    private String movementNumber;

    private UUID productId;
    private String productName;
    private String productCode;

    private UUID variantId;
    private String variantSku;

    private MovementType movementType;
    private Integer quantity;
    private Integer previousQuantity;
    private Integer newQuantity;

    private BigDecimal unitCost;
    private BigDecimal totalCost;

    private UUID fromWarehouseId;
    private String fromWarehouseName;
    private UUID toWarehouseId;
    private String toWarehouseName;

    private UUID referenceId;
    private ReferenceType referenceType;

    private String notes;
    private UUID createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
}