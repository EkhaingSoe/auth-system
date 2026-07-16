package com.example.auth_system.order.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.order.dto.request.Payment.CreateEcommercePaymentRequest;
import com.example.auth_system.order.dto.request.Payment.CreatePaymentRequest;
import com.example.auth_system.order.dto.response.paymentResponse.EcommercePaymentResponse;
import com.example.auth_system.order.dto.response.paymentResponse.PaymentResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.Payment;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.enums.PaymentStatus;
import com.example.auth_system.order.mapper.PaymentMapper;
import com.example.auth_system.order.repository.OrderRepository;
import com.example.auth_system.order.repository.PaymentRepository;
import com.example.auth_system.order.service.OrderStatusService;
import com.example.auth_system.order.service.PaymentService;
import com.example.auth_system.payment_gateway.kpay.KPayPaymentService;
import com.example.auth_system.payment_gateway.kpay.KPayResponse;
import com.example.auth_system.payment_gateway.kpay.KPayWebhookRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final OrderStatusService orderStatusService;
    private final KPayPaymentService kPayPaymentService;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        // Implement the logic to create a payment
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new ResourceNotFoundException("Order is not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Order has already been paid");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot create payment for a cancelled order");
        }

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException("Cannot create payment for a delivered order");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Payment payment = paymentMapper.toEntity(request, order, currentUser);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setCompletedDate(LocalDateTime.now());
        payment.setPaymentNumber(generatePaymentNumber());
        order.setPaymentStatus(PaymentStatus.PAID);
        orderStatusService.changeStatus(order, OrderStatus.PROCESSING, currentUser, "Payment completed");
        Payment savedPayment = paymentRepository.save(payment);
        orderRepository.save(order); // Save the updated order with the new status
        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public EcommercePaymentResponse createEcommercePayment(CreateEcommercePaymentRequest request) {
        // Implement the logic to create a payment
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new ResourceNotFoundException("Order is not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Order has already been paid");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot create payment for a cancelled order");
        }

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException("Cannot create payment for a delivered order");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Payment payment = paymentMapper.toEcommerceEntity(request, order, currentUser);
        payment.setPaymentNumber(generatePaymentNumber());
        Payment savedPayment = paymentRepository.save(payment);
        // this is sending from my backend server to kpay server
        KPayResponse kPayResponse = kPayPaymentService.createPayment(savedPayment);
        // Save gateway information
        savedPayment.setGatewayReference(kPayResponse.getPaymentId());
        savedPayment.setGatewayName("KPAY");

        paymentRepository.save(savedPayment);
        EcommercePaymentResponse response = paymentMapper.toEcommercePaymentResponse(savedPayment);
        response.setPaymentUrl(kPayResponse.getPaymentUrl());
        response.setQrCode(kPayResponse.getQrCode());

        return response;
    }

    @Override
    @Transactional
    public void handleKPayWebhook(KPayWebhookRequest request) {

        Payment payment = paymentRepository
                .findByGatewayReference(request.getPaymentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Payment not found"));

        if (request.getStatus().equals("SUCCESS")) {

            payment.setPaymentStatus(PaymentStatus.PAID);

            payment.setTransactionId(request.getTransactionId());

            payment.setCompletedDate(LocalDateTime.now());

            paymentRepository.save(payment);

            Order order = payment.getOrder();

            order.setPaymentStatus(PaymentStatus.PAID);

            orderStatusService.changeStatus(
                    order,
                    OrderStatus.PROCESSING,
                    payment.getCreatedBy(),
                    "Payment completed");

            orderRepository.save(order);

        }

    }

    @Override
    public PaymentResponse completePayment(UUID paymentId) {
        // Implement the logic to create a payment

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new ResourceNotFoundException("Payment is not found"));

        return null;
    }

    private String generatePaymentNumber() {
        return "PAY-" + System.currentTimeMillis();
    }
}
