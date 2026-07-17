package com.example.auth_system.order.dto.response.paymentResponse;

import com.example.auth_system.order.enums.PaymentMethod;
import com.example.auth_system.order.enums.PaymentStatus;
import com.example.auth_system.order.order.dto.orderResponse.CustomerInfoResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private UUID id;
    private String paymentNumber;
    private UUID orderId;
    private String orderNumber;

    private CustomerInfoResponse customerInfo;

    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private String transactionId;

    private String cardLastFour;
    private String cardBrand;

    private String bankName;
    private String bankAccountLastFour;

    private String gatewayName;
    private String gatewayReference;

    private String currency;
    private BigDecimal exchangeRate;

    private LocalDateTime paymentDate;
    private LocalDateTime completedDate;
    private LocalDateTime refundedDate;

    private String refundReason;
    private BigDecimal refundAmount;
    private String refundTransactionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}