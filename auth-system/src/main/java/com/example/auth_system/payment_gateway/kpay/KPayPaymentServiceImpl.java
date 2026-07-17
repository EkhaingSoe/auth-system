package com.example.auth_system.payment_gateway.kpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.auth_system.order.payment.entity.Payment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KPayPaymentServiceImpl implements KPayPaymentService {

    private final RestTemplate restTemplate;
    @Value("${kpay.api.url}")
    private String kpayUrl;

    @Override
    public KPayResponse createPayment(Payment payment) {

        KPayRequest request = KPayRequest.builder()
                .merchantId("SHOP001")
                .merchantOrderId(payment.getOrder().getOrderNumber())
                .amount(payment.getAmount())
                .currency("MMK")
                .callbackUrl("https://myshop.com/api/payments/kpay/webhook")
                .returnUrl("https://myshop.com/payment-result")
                .build();

        // POST https://kpay-api.com/payment/create
        ResponseEntity<KPayResponse> response = restTemplate.postForEntity(
                kpayUrl,
                request,
                KPayResponse.class);

        return response.getBody();

    }

    @Override
    public boolean verifySignature(String signature) {

        return true;
    }

    @Override
    public void handleWebhook(KPayWebhookRequest request) {

    }
}
