package com.example.auth_system.order.dto.request.Payment;

import com.example.auth_system.order.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;

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