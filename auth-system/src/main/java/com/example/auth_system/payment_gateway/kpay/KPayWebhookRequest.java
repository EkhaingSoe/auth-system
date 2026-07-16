package com.example.auth_system.payment_gateway.kpay;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KPayWebhookRequest {

    private String paymentId;

    private String merchantOrderId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private String transactionId;

    private String paidAt;

    private String signature;
}
