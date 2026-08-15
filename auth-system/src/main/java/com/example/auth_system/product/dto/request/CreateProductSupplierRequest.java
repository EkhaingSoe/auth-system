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
public class CreateProductSupplierRequest {

    @NotNull(message = "Supplier ID is required")
    private UUID supplierId;

    private String supplierProductCode;

    private BigDecimal supplierPrice;

    @Builder.Default
    private Integer leadTimeDays = 7;

    @Builder.Default
    private Boolean isPrimary = false;
}