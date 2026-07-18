package com.example.auth_system.payment_gateway.kpay;

import java.math.BigDecimal;

import com.example.auth_system.order.payment.entity.Payment;

public interface KPayGateway {

    KPayResponse createPayment(Payment payment);

    KPayRefundResponse refundPayment(Payment payment, BigDecimal amount);

    boolean verifySignature(KPayWebhookRequest request);

}
