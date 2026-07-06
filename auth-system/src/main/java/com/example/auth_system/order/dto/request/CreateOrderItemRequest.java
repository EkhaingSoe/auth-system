package com.example.auth_system.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemRequest {

    @NotNull
    private UUID productId;

    private UUID variantId;

    @NotNull
    private Integer quantity;

    private BigDecimal unitPrice;
}