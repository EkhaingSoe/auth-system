package com.example.auth_system.payment_gateway.kpay;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KPayRequest {

    private String merchantId;

    private String merchantOrderId;

    private BigDecimal amount;

    private String currency;

    private String description;

    private String callbackUrl;

    private String returnUrl;

    private String timestamp;

    private String signature;
}
