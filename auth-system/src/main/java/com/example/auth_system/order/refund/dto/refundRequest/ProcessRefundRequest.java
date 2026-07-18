package com.example.auth_system.order.refund.dto.refundRequest;

import com.example.auth_system.order.refund.enums.RefundMethod;
import com.example.auth_system.order.refund.enums.RefundType;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRefundRequest {

    @NotNull
    private UUID orderId;

    private UUID paymentId;

    @NotNull
    private BigDecimal refundAmount;

    @NotNull
    private String refundReason;

    @NotNull
    private RefundType refundType;

    @NotNull
    private RefundMethod refundMethod;

    // replaced JsonNode
    private List<RefundItemRequest> refundItems;

    // private UUID approvedBy;

}