package com.example.auth_system.payment_gateway.kpay;

import com.example.auth_system.order.entity.Payment;

public interface KPayPaymentService {

    KPayResponse createPayment(Payment payment);

    boolean verifySignature(String signature);

    void handleWebhook(KPayWebhookRequest request);

}
