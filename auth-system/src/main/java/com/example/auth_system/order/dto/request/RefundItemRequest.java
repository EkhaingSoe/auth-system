package com.example.auth_system.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundItemRequest {

    @NotNull
    private UUID orderItemId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal refundAmount;

}
