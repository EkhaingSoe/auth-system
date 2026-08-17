package com.example.auth_system.product.dto.response;

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
public class ProductSupplierResponse {

    private UUID id;

    private UUID supplierId;

    private String supplierName;

    private String supplierCode;

    private String supplierProductCode;

    private BigDecimal purchasePrice;

    private Integer leadTimeDays;

    private Boolean isPrimary;
}