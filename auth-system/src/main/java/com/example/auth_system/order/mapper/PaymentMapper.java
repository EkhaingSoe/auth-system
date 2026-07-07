package com.example.auth_system.order.mapper;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.dto.request.CreatePaymentRequest;
import com.example.auth_system.order.dto.response.orderResponse.CustomerInfoResponse;
import com.example.auth_system.order.dto.response.paymentResponse.PaymentResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.Payment;

public class PaymentMapper {

    public Payment toEntity(CreatePaymentRequest request, Order order, User createdBy) {
        Payment payment = Payment.builder()
                .order(order)
                .customer(order.getCustomer())
                .createdBy(createdBy)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionId(request.getTransactionId())
                .cardLastFour(request.getCardLastFour())
                .cardBrand(request.getCardBrand())
                .bankName(request.getBankName())
                .bankAccount(request.getBankAccount())
                .build();
        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentNumber(payment.getPaymentNumber())
                .orderId(payment.getOrder().getId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .customerInfo(CustomerInfoResponse.builder()
                        .id(payment.getCustomer().getId())
                        .email(payment.getCustomer().getEmail())
                        .name(payment.getCustomer() != null
                                ? payment.getCustomer().getFirstName() + " " + payment.getCustomer().getLastName()
                                : null)
                        .build())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .cardLastFour(payment.getCardLastFour())
                .cardBrand(payment.getCardLastFour())
                .bankName(payment.getBankName())
                .bankAccountLastFour(payment.getBankAccount())
                .gatewayName(payment.getGatewayName())
                .gatewayReference(payment.getGatewayReference())
                .currency(payment.getCurrency())
                .exchangeRate(payment.getExchangeRate())
                .paymentDate(payment.getPaymentDate())
                .completedDate(payment.getCompletedDate())
                .refundedDate(payment.getRefundedDate())
                .refundReason(payment.getRefundReason())
                .refundAmount(payment.getRefundAmount())
                .refundTransactionId(payment.getRefundTransactionId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

}
