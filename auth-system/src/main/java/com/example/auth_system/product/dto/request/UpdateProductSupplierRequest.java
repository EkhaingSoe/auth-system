package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductSupplierRequest {

    private UUID id;

    @NotNull(message = "Supplier ID is required")
    private UUID supplierId;

    private String supplierProductCode;

    private BigDecimal supplierPrice;

    private Integer leadTimeDays;

    private Boolean isPrimary;
}