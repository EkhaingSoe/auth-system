package com.example.auth_system.order.payment.dto.paymentRequest;

import lombok.*;

import java.math.BigDecimal;

import com.example.auth_system.order.payment.enums.PaymentMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentWebhookRequest {

    // Gateway payment ID (Stripe/KPay/etc.)
    private String gatewayPaymentId;

    // Your order reference (orderNumber or UUID)
    private String orderReference;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    // SUCCESS / FAILED / PENDING
    private String status;

    // Security verification
    private String signature;
}