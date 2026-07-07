package com.example.auth_system.order.mapper;

import java.util.List;

import com.example.auth_system.order.dto.request.ProcessRefundRequest;
import com.example.auth_system.order.dto.response.RefundResponse.RefundResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.Payment;
import com.example.auth_system.order.entity.Refund;
import com.example.auth_system.order.entity.RefundItem;

public class RefundMapper {

    public Refund toEntity(ProcessRefundRequest request, Order order, Payment payment, List<RefundItem> refundItems) {
        Refund refund = Refund.builder()
                .order(order)
                .payment(payment)
                .refundAmount(request.getRefundAmount())
                .refundReason(request.getRefundReason())
                .refundType(request.getRefundType())
                .refundMethod(request.getRefundMethod())
                .refundItems(refundItems)
                .build();
        return refund;
    }

    public RefundResponse toResponse(Refund refund) {
        if (refund == null) {
            return null;
        }

        return RefundResponse.builder()
                .build();
    }

}
