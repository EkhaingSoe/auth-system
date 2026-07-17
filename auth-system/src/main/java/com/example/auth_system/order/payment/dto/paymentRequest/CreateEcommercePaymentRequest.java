package com.example.auth_system.order.payment.dto.paymentRequest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.auth_system.order.payment.enums.PaymentMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEcommercePaymentRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

    // from Stripe / KPay / PayPal
    private String paymentToken;

    private String returnUrl;
}