package com.example.auth_system.order.dto.response.paymentResponse;

import java.util.UUID;

import com.example.auth_system.order.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcommercePaymentResponse {

    private UUID paymentId;

    private String paymentNumber;

    private PaymentStatus paymentStatus;

    private String gatewayName;

    private String gatewayReference;

    private String paymentUrl;

    private String qrCode;

}
