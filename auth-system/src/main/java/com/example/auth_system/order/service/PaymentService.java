package com.example.auth_system.order.service;

import com.example.auth_system.order.dto.request.Payment.CreateEcommercePaymentRequest;
import com.example.auth_system.order.dto.request.Payment.CreatePaymentRequest;
import com.example.auth_system.order.dto.response.paymentResponse.EcommercePaymentResponse;
import com.example.auth_system.order.dto.response.paymentResponse.PaymentResponse;
import com.example.auth_system.payment_gateway.kpay.KPayWebhookRequest;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    EcommercePaymentResponse createEcommercePayment(CreateEcommercePaymentRequest request);

    void handleKPayWebhook(KPayWebhookRequest request);

    // PaymentResponse refundPayment(UUID paymentId, String reason);
    PaymentResponse getPaymentById(UUID paymentId);

    List<PaymentResponse> getPaymentsByOrder(UUID orderId);

    List<PaymentResponse> getPaymentsByCustomer(UUID customerId);

    void deletePayment(UUID paymentId);
}