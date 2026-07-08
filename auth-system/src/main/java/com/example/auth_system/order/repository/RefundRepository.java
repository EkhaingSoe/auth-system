package com.example.auth_system.order.repository;

import com.example.auth_system.order.entity.Refund;
import com.example.auth_system.order.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {

    Optional<Refund> findByRefundNumber(String refundNumber);

    List<Refund> findByOrderId(UUID orderId);

    List<Refund> findByPaymentId(UUID paymentId);

    List<Refund> findByRefundStatus(RefundStatus refundStatus);

    List<Refund> findByCreatedById(UUID userId);

}