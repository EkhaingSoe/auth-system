package com.example.auth_system.order.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.customer.repository.CustomerRepository;
import com.example.auth_system.order.dto.request.Payment.CreateEcommercePaymentRequest;
import com.example.auth_system.order.dto.request.Payment.CreatePaymentRequest;
import com.example.auth_system.order.dto.response.paymentResponse.EcommercePaymentResponse;
import com.example.auth_system.order.dto.response.paymentResponse.PaymentResponse;
import com.example.auth_system.order.entity.Payment;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.enums.PaymentStatus;
import com.example.auth_system.order.mapper.PaymentMapper;
import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.repository.OrderRepository;
import com.example.auth_system.order.repository.PaymentRepository;
import com.example.auth_system.order.service.OrderStatusService;
import com.example.auth_system.order.service.PaymentService;
import com.example.auth_system.payment_gateway.kpay.KPayPaymentService;
import com.example.auth_system.payment_gateway.kpay.KPayResponse;
import com.example.auth_system.payment_gateway.kpay.KPayWebhookRequest;

import org.springframework.transaction.annotation.Transactional;
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
    private final CustomerRepository customerRepository;

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
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(UUID paymentId) {

        Payment payment = paymentRepository.findByIdAndDeletedFalse(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByOrder(UUID orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        List<Payment> payments = paymentRepository.findByOrderIdAndDeletedFalse(orderId);

        return payments.stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByCustomer(UUID customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found");
        }

        List<Payment> payments = paymentRepository.findByCustomerIdAndDeletedFalse(customerId);

        return payments.stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deletePayment(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        payment.setDeleted(true);
        payment.setDeletedAt(LocalDateTime.now());
        payment.setDeletedBy(currentUser);

        paymentRepository.save(payment);
    }

    private String generatePaymentNumber() {
        return "PAY-" + System.currentTimeMillis();
    }
}
