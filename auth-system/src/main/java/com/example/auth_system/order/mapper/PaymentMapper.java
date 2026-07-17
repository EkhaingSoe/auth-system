package com.example.auth_system.order.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.dto.request.Payment.CreateEcommercePaymentRequest;
import com.example.auth_system.order.dto.request.Payment.CreatePaymentRequest;
import com.example.auth_system.order.dto.response.paymentResponse.EcommercePaymentResponse;
import com.example.auth_system.order.dto.response.paymentResponse.PaymentResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.Payment;
import com.example.auth_system.order.enums.PaymentStatus;
import com.example.auth_system.order.order.dto.orderResponse.CustomerInfoResponse;

@Component
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

        public Payment toEcommerceEntity(CreateEcommercePaymentRequest request, Order order, User createdBy) {
                Payment payment = Payment.builder()
                                .order(order)
                                .customer(order.getCustomer())
                                .createdBy(createdBy)
                                .amount(request.getAmount())
                                .paymentMethod(request.getPaymentMethod())
                                .paymentStatus(PaymentStatus.PROCESSING)
                                .gatewayName("KPay")
                                .build();
                return payment;
        }

        public PaymentResponse toResponse(Payment payment) {
                if (payment == null) {
                        return null;
                }

                return PaymentResponse.builder()
                                .id(payment.getCustomer().getId())
                                .paymentNumber(payment.getPaymentNumber())
                                .orderId(payment.getOrder().getId())
                                .orderNumber(payment.getOrder().getOrderNumber())
                                .customerInfo(payment.getCustomer() == null
                                                ? null
                                                : CustomerInfoResponse.builder()
                                                                .id(payment.getCustomer().getId())
                                                                .email(payment.getCustomer().getEmail())
                                                                .name(payment.getCustomer() != null
                                                                                ? payment.getCustomer().getFirstName()
                                                                                                + " "
                                                                                                + payment.getCustomer()
                                                                                                                .getLastName()
                                                                                : null)
                                                                .build())
                                .amount(payment.getAmount())
                                .paymentMethod(payment.getPaymentMethod())
                                .paymentStatus(payment.getPaymentStatus())
                                .transactionId(payment.getTransactionId())
                                .cardLastFour(payment.getCardLastFour())
                                .cardBrand(payment.getCardBrand())
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

        public EcommercePaymentResponse toEcommercePaymentResponse(Payment payment) {

                if (payment == null) {
                        return null;
                }

                return EcommercePaymentResponse.builder()
                                .paymentId(payment.getId())
                                .paymentNumber(payment.getPaymentNumber())
                                .paymentStatus(payment.getPaymentStatus())
                                .gatewayName(payment.getGatewayName())
                                .gatewayReference(payment.getGatewayReference())
                                .build();
        }

}
