package com.example.auth_system.payment_gateway.kpay;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.auth_system.order.payment.entity.Payment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KPayGatewayImpl implements KPayGateway {

    private final RestTemplate restTemplate;

    @Value("${kpay.api.url}")
    private String kpayUrl;

    @Value("${kpay.api.refund-url}")
    private String refundUrl;

    @Value("${kpay.merchant-secret}")
    private String merchantSecret;

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
    public KPayRefundResponse refundPayment(
            Payment payment,
            BigDecimal amount) {

        KPayRefundRequest request = KPayRefundRequest.builder()
                .merchantId("SHOP001")
                .refundNumber("REF-000001")
                .paymentId(payment.getGatewayReference())
                .amount(amount)
                .currency("MMK")
                .reason("Customer requested refund")
                .build();

        ResponseEntity<KPayRefundResponse> response = restTemplate.postForEntity(
                refundUrl,
                request,
                KPayRefundResponse.class);

        return response.getBody();
    }

    @Override
    public boolean verifySignature(KPayWebhookRequest request) {

        String data = request.getPaymentId()
                + request.getMerchantOrderId()
                + request.getAmount()
                + request.getStatus();

        String generatedSignature = generateHmac(data, merchantSecret);

        return generatedSignature.equals(
                request.getSignature());
    }

    private String generateHmac(String data, String secret) {

        try {

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");

            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate signature");
        }
    }

}
