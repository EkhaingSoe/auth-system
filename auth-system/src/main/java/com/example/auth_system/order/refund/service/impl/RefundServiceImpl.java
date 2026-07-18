package com.example.auth_system.order.refund.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CurrentUserService;
import com.example.auth_system.order.order.dto.orderResponse.OrderResponse;
import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.order.entity.OrderItem;
import com.example.auth_system.order.order.repository.OrderItemRepository;
import com.example.auth_system.order.order.repository.OrderRepository;
import com.example.auth_system.order.payment.entity.Payment;
import com.example.auth_system.order.payment.enums.PaymentStatus;
import com.example.auth_system.order.payment.repository.PaymentRepository;
import com.example.auth_system.order.refund.dto.refundRequest.ProcessRefundRequest;
import com.example.auth_system.order.refund.dto.refundRequest.RefundItemRequest;
import com.example.auth_system.order.refund.dto.refundResponse.RefundResponse;
import com.example.auth_system.order.refund.entity.Refund;
import com.example.auth_system.order.refund.entity.RefundItem;
import com.example.auth_system.order.refund.enums.RefundStatus;
import com.example.auth_system.order.refund.mapper.RefundItemMapper;
import com.example.auth_system.order.refund.mapper.RefundMapper;
import com.example.auth_system.order.refund.repository.RefundRepository;
import com.example.auth_system.order.refund.service.RefundService;
import com.example.auth_system.payment_gateway.kpay.KPayGateway;
import com.example.auth_system.payment_gateway.kpay.KPayRefundResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefundServiceImpl implements RefundService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CurrentUserService currentUserService;
    private final OrderItemRepository orderItemRepository;
    private final RefundItemMapper refundItemMapper;
    private final RefundMapper refundMapper;
    private final KPayGateway KPayGateWay;

    @Override
    public RefundResponse processRefund(ProcessRefundRequest request) {

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BusinessException(
                    "Only paid orders can be refunded");
        }

        Payment payment = paymentRepository.findById(request.getPaymentId()).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found"));

        if (payment.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BusinessException(
                    "Only paid payment can be refunded");
        }

        BigDecimal alreadyRefunded = refundRepository.findByPaymentId(payment.getId())
                .stream() // get refund list -> refund has refund amount -> get total already refund
                          // amount
                .filter(refund -> refund.getRefundStatus() == RefundStatus.COMPLETED)
                .map(Refund::getRefundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal refundableAmount = payment.getAmount().subtract(alreadyRefunded);

        if (request.getRefundAmount().compareTo(refundableAmount) > 0) {

            throw new BusinessException(
                    "Refund amount exceeds refundable amount");
        }

        User currentUser = currentUserService.getCurrentUser();

        List<RefundItemRequest> refundItemRequests = request.getRefundItems() != null
                ? request.getRefundItems()
                : new ArrayList<>();

        List<RefundItem> refundItems = new ArrayList<>(); // set variable

        for (RefundItemRequest itemRequest : refundItemRequests) {

            OrderItem orderItem = orderItemRepository.findById(itemRequest.getOrderItemId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Order item not found"));

            if (itemRequest.getQuantity() > orderItem.getQuantity()) {
                throw new BusinessException(
                        "Refund quantity exceeds purchased quantity");
            }

            RefundItem refundItem = refundItemMapper.toEntity(orderItem, itemRequest);

            refundItems.add(refundItem);
        }

        Refund refund = refundMapper.toEntity(request, order, payment, refundItems, currentUser);

        for (RefundItem item : refundItems) {
            refund.addRefundItem(item);
        }

        refund.setRefundStatus(RefundStatus.PENDING);

        Refund saveRefund = refundRepository.save(refund);

        return refundMapper.toResponse(saveRefund);

    }

    @Override
    @Transactional(readOnly = true)
    public RefundResponse getRefundById(UUID refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return refundMapper.toResponse(refund);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getRefundsByOrder(UUID orderId) {

        List<Refund> refunds = refundRepository.findByOrderId(orderId);
        return refunds.stream()
                .map(refundMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getRefundsByPayment(UUID paymentId) {

        List<Refund> refunds = refundRepository.findByPaymentId(paymentId);
        return refunds.stream()
                .map(refundMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getRefundsByCustomer(UUID customerId) {

        List<Refund> refunds = refundRepository.findByCreatedById(customerId);
        return refunds.stream()
                .map(refundMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getPendingRefunds() {

        List<Refund> refunds = refundRepository.findByRefundStatus(RefundStatus.PENDING);
        return refunds.stream()
                .map(refundMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RefundResponse approveRefund(UUID refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));

        User admin = currentUserService.getCurrentUser();

        refund.setApprovedBy(admin);
        refund.setRefundStatus(RefundStatus.APPROVED);

        return refundMapper.toResponse(refund);
    }

    @Override
    @Transactional
    public RefundResponse rejectRefund(UUID refundId, String reason) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));

        refund.setRefundStatus(RefundStatus.REJECTED);
        refund.setRefundReason(reason);

        return refundMapper.toResponse(refund);
    }

    @Override
    @Transactional
    public RefundResponse completeRefund(UUID refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));

        if (refund.getRefundStatus() != RefundStatus.APPROVED) {

            throw new BusinessException(
                    "Only approved refund can be completed");
        }

        Payment payment = refund.getPayment();

        KPayRefundResponse response = KPayGateWay.refundPayment(payment, refund.getRefundAmount());

        if (!response.isSuccess()) {

            refund.setRefundStatus(RefundStatus.REJECTED);
            refundRepository.save(refund);
            throw new BusinessException("KPay refund failed");
        }

        refund.setRefundStatus(RefundStatus.COMPLETED);
        return refundMapper.toResponse(refund);
    }

}
