package com.example.auth_system.order.dto.response.RefundResponse;

import com.example.auth_system.auth.dto.response.UserInfoResponse;
import com.example.auth_system.order.enums.RefundMethod;
import com.example.auth_system.order.enums.RefundStatus;
import com.example.auth_system.order.enums.RefundType;
import com.example.auth_system.order.order.dto.orderResponse.CustomerInfoResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    private UUID id;
    private String refundNumber;
    private UUID orderId;
    private String orderNumber;

    private UUID paymentId;
    private String paymentNumber;

    private BigDecimal refundAmount;
    private String refundReason;
    private RefundType refundType;
    private RefundMethod refundMethod;
    private RefundStatus refundStatus;

    private List<RefundItemResponse> refundItems;

    private CustomerInfoResponse createdBy;
    private UserInfoResponse approvedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
