package com.example.auth_system.order.entity;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.order.enums.PaymentMethod;
import com.example.auth_system.order.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "payment_number", unique = true, nullable = false, length = 20)
    private String paymentNumber;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", length = 20)
    private String cardBrand;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "currency")
    @Builder.Default
    private String currency = "MMK";

    @Column(name = "exchange_rate")
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "gateway_name")
    private String gatewayName;

    @Column(name = "gateway_reference")
    private String gatewayReference;

    @Column(name = "payment_date")
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "refunded_date")
    private LocalDateTime refundedDate;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @Column(name = "refund_transaction_id")
    private String refundTransactionId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}