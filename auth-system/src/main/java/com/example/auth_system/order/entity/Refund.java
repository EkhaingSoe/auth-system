package com.example.auth_system.order.entity;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.dto.request.RefundItemRequest;
import com.example.auth_system.order.enums.RefundMethod;
import com.example.auth_system.order.enums.RefundType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "refunds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "refund_number", unique = true, nullable = false, length = 20)
    private String refundNumber;

    @Column(name = "refund_amount", nullable = false)
    private BigDecimal refundAmount;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "refund_type")
    private RefundType refundType; // FULL, PARTIAL

    @Column(name = "refund_method")
    private RefundMethod refundMethod; // ORIGINAL_PAYMENT, STORE_CREDIT, BANK_TRANSFER

    @Column(name = "refund_status")
    @Builder.Default
    private String refundStatus = "PENDING"; // PENDING, APPROVED, PROCESSING, COMPLETED, REJECTED

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "refund_items", columnDefinition = "jsonb")
    private List<RefundItem> refundItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}