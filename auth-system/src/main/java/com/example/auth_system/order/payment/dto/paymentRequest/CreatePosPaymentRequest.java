package com.example.auth_system.order.payment.dto.paymentRequest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.auth_system.order.payment.enums.PaymentMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePosPaymentRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Optional reference (bank slip, QR ref, etc.)
    private String transactionId;

    // CARD (manual entry or POS terminal summary)
    private String cardLastFour;
    private String cardBrand;

    // BANK TRANSFER
    private String bankName;
    private String bankAccount;

    @NotNull(message = "Created by is required")
    private UUID createdBy;
}