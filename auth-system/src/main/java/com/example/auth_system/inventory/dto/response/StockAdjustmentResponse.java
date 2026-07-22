package com.example.auth_system.inventory.dto.response;

import com.example.auth_system.inventory.enums.AdjustmentStatus;
import com.example.auth_system.inventory.enums.AdjustmentType;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentResponse {

    private UUID id;
    private String adjustmentNumber;

    // Product Information
    private UUID productId;
    private String productName;
    private String productCode;

    // Variant Information
    private UUID variantId;
    private String variantSku;
    private JsonNode variantAttributes;

    // Warehouse Information
    private UUID warehouseId;
    private String warehouseName;

    // Adjustment Details
    private AdjustmentType adjustmentType;
    private Integer oldQuantity;
    private Integer newQuantity;
    private Integer difference;

    // Status & Approval
    private AdjustmentStatus status;
    private UUID approvedBy;
    private String approvedByName;
    private LocalDateTime approvedAt;

    // Audit
    private UUID createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional Info
    private String reason;
}