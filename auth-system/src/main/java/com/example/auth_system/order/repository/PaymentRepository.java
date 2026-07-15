package com.example.auth_system.order.repository;

import com.example.auth_system.order.entity.Payment;
import com.example.auth_system.order.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByPaymentNumber(String paymentNumber);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByGatewayReference(String gatewayReference);

    List<Payment> findByOrderId(UUID orderId);

    List<Payment> findByCustomerId(UUID customerId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByOrderIdAndPaymentStatus(
            UUID orderId,
            PaymentStatus paymentStatus);

    boolean existsByTransactionId(String transactionId);

    Optional<Payment> findFirstByOrderIdOrderByCreatedAtDesc(UUID orderId);

}
