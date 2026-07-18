package com.example.auth_system.order.refund.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CurrentUserService;
import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.order.repository.OrderRepository;
import com.example.auth_system.order.payment.entity.Payment;
import com.example.auth_system.order.payment.enums.PaymentStatus;
import com.example.auth_system.order.payment.repository.PaymentRepository;
import com.example.auth_system.order.refund.dto.refundRequest.ProcessRefundRequest;
import com.example.auth_system.order.refund.dto.refundResponse.RefundResponse;
import com.example.auth_system.order.refund.entity.Refund;
import com.example.auth_system.order.refund.enums.RefundStatus;
import com.example.auth_system.order.refund.repository.RefundRepository;
import com.example.auth_system.order.refund.service.RefundService;

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

        return null;

    }

}
