package com.example.auth_system.payment_gateway.kpay;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPayRefundRequest {

    // Merchant ID provided by KPay
    private String merchantId;

    // Your refund number
    private String refundNumber;

    // Original KPay payment ID
    private String paymentId;

    // Refund amount
    private BigDecimal amount;

    // MMK
    private String currency;

    // Refund reason
    private String reason;
}
