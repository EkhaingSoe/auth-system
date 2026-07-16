package com.example.auth_system.payment_gateway.kpay;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class KPayResponse {

    // KPay transaction id
    private String paymentId;

    private String merchantOrderId;

    private BigDecimal amount;

    private String currency;

    private String qrCode;

    private String paymentUrl;

    private String status;

    private String message;

    private String signature;

}

// {
// "paymentId":"KP123456",
// "qrCode":"xxxx",
// "paymentUrl":"https://kpay.com/pay/123",
// "status":"PENDING"
// }
