package com.example.auth_system.order.refund.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.payment.entity.Payment;
import com.example.auth_system.order.refund.dto.refundRequest.ProcessRefundRequest;
import com.example.auth_system.order.refund.dto.refundResponse.RefundResponse;
import com.example.auth_system.order.refund.entity.Refund;
import com.example.auth_system.order.refund.entity.RefundItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefundMapper {

    private final RefundItemMapper refundItemMapper;

    public Refund toEntity(ProcessRefundRequest request, Order order, Payment payment, List<RefundItem> refundItems,
            User currentUser) {
        Refund refund = Refund.builder()
                .order(order)
                .payment(payment)
                .refundAmount(request.getRefundAmount())
                .refundReason(request.getRefundReason())
                .refundType(request.getRefundType())
                .refundMethod(request.getRefundMethod())
                .refundItems(refundItems)
                .createdBy(currentUser)
                .build();
        return refund;
    }

    public RefundResponse toResponse(Refund refund) {
        if (refund == null) {
            return null;
        }

        return RefundResponse.builder()
                .id(refund.getId())
                .refundNumber(refund.getRefundNumber())
                .orderId(refund.getOrder() != null ? refund.getOrder().getId() : null)
                .orderNumber(refund.getOrder() != null ? refund.getOrder().getOrderNumber() : null)
                .paymentId(refund.getPayment() != null ? refund.getPayment().getId() : null)
                .paymentNumber(refund.getPayment() != null ? refund.getPayment().getPaymentNumber() : null)
                .refundAmount(refund.getRefundAmount())
                .refundReason(refund.getRefundReason())
                .refundType(refund.getRefundType())
                .refundMethod(refund.getRefundMethod())
                .refundStatus(refund.getRefundStatus())
                .refundItems(refund.getRefundItems() != null
                        ? refund.getRefundItems().stream().map(refundItemMapper::toResponse).toList()
                        : null)
                .build();
    }

}
