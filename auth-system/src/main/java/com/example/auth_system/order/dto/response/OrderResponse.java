package com.example.auth_system.order.dto.response;

import com.example.auth_system.auth.dto.response.LoginResponse.UserInfo;
import com.example.auth_system.order.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private String orderNumber;
    private OrderType orderType;

    private CustomerInfoResponse customer;

    private UserInfo createdBy;
    private UserInfo approvedBy;

    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private FulfillmentStatus fulfillmentStatus;

    private OrderTimelineResponse timeline;

    private OrderSummaryResponse summary;

    private AddressInfoResponse shippingAddress;
    private AddressInfoResponse billingAddress;

    private String customerNotes;
    private String internalNotes;

    private List<OrderItemResponse> items;
    private List<PaymentResponse> payments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}