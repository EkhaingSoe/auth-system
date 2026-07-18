package com.example.auth_system.payment_gateway.kpay;

import com.example.auth_system.order.refund.enums.RefundStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPayRefundResponse {

    // Refund ID from KPay
    private String refundId;

    // Original payment ID
    private String paymentId;

    // SUCCESS / PENDING / FAILED
    private String status;

    private String message;

    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }
}