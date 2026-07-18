package com.example.auth_system.order.refund.service;

import com.example.auth_system.order.refund.dto.refundRequest.ProcessRefundRequest;
import com.example.auth_system.order.refund.dto.refundResponse.RefundResponse;

import java.util.List;
import java.util.UUID;

public interface RefundService {

    RefundResponse processRefund(ProcessRefundRequest request);

    // RefundResponse getRefundById(UUID refundId);

    // List<RefundResponse> getRefundsByOrder(UUID orderId);

    // List<RefundResponse> getRefundsByPayment(UUID paymentId);

    // List<RefundResponse> getRefundsByCustomer(UUID customerId);

    // List<RefundResponse> getPendingRefunds();

    // RefundResponse approveRefund(UUID refundId);

    // RefundResponse rejectRefund(UUID refundId, String reason);

    // RefundResponse completeRefund(UUID refundId);

}