package com.example.auth_system.order.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.auth_system.order.dto.response.orderResponse.UserInfoResponse;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.order.dto.request.Order.CreateOrderRequest;
import com.example.auth_system.order.dto.response.orderResponse.AddressInfoResponse;
import com.example.auth_system.order.dto.response.orderResponse.CustomerInfoResponse;
import com.example.auth_system.order.dto.response.orderResponse.OrderResponse;
import com.example.auth_system.order.dto.response.orderResponse.OrderSummaryResponse;
import com.example.auth_system.order.dto.response.orderResponse.OrderTimelineResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.OrderItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

        private final OrderItemMapper orderItemMapper;

        // Create Request -> Entity
        public Order toEntity(CreateOrderRequest request, Customer customer,
                        List<OrderItem> orderItems,
                        BigDecimal subtotal) {

                Order order = Order.builder()
                                .orderType(request.getOrderType())
                                .customer(customer) // Service ကနေ လှမ်းပေးလိုက်တဲ့ Customer
                                .subtotal(subtotal) // Service ကနေ တွက်ချက်ပေးလိုက်တဲ့ ပမာဏ
                                .grandTotal(subtotal) // (Tax/Discount မရှိသေးရင် subtotal နဲ့ တူမယ်)
                                .currency("MMK") // Default value
                                .customerNotes(request.getCustomerNotes())
                                .couponCode(request.getCouponCode())
                                .build();

                if (request.getShippingAddress() != null) {
                        order.setShippingAddress(request.getShippingAddress().getAddress());
                        order.setShippingCity(request.getShippingAddress().getCity());
                        order.setShippingState(request.getShippingAddress().getState());
                        order.setShippingPostalCode(request.getShippingAddress().getPostalCode());
                        order.setShippingCountry(request.getShippingAddress().getCountry());
                }

                if (request.getBillingAddress() != null) {
                        order.setBillingAddress(request.getBillingAddress().getAddress());
                        order.setBillingCity(request.getBillingAddress().getCity());
                        order.setBillingState(request.getBillingAddress().getState());
                        order.setBillingPostalCode(request.getBillingAddress().getPostalCode());
                        order.setBillingCountry(request.getBillingAddress().getCountry());
                }

                orderItems.forEach(order::addItem);

                return order;

        }

        public OrderResponse toResponse(Order order) {
                if (order == null) {
                        return null;
                }

                return OrderResponse.builder()
                                .id(order.getId())
                                .orderNumber(order.getOrderNumber())
                                .orderType(order.getOrderType())
                                .customer(
                                                order.getCustomer() != null
                                                                ? CustomerInfoResponse.builder()
                                                                                .id(order.getCustomer().getId())
                                                                                .build()
                                                                : null)

                                .createdBy(
                                                order.getCreatedBy() != null
                                                                ? UserInfoResponse.builder()
                                                                                .id(order.getCreatedBy().getId())
                                                                                .username(order.getCreatedBy()
                                                                                                .getUsername())
                                                                                .build()
                                                                : null)

                                .approvedBy(
                                                order.getApprovedBy() != null
                                                                ? UserInfoResponse.builder()
                                                                                .id(order.getApprovedBy().getId())
                                                                                .username(order.getApprovedBy()
                                                                                                .getUsername())
                                                                                .build()
                                                                : null)

                                .orderStatus(order.getOrderStatus())
                                .paymentStatus(order.getPaymentStatus())
                                .fulfillmentStatus(order.getFulfillmentStatus())
                                .timeline(OrderTimelineResponse.builder()
                                                .orderDate(order.getOrderDate())
                                                .approvedDate(order.getApprovedDate())
                                                .processingDate(order.getProcessingDate())
                                                .shippedDate(order.getShippedDate())
                                                .deliveredDate(order.getDeliveredDate())
                                                .cancelledDate(order.getCancelledDate())
                                                .build())
                                .summary(OrderSummaryResponse.builder()
                                                .subtotal(order.getSubtotal())
                                                .discountAmount(order.getDiscountAmount())
                                                .taxAmount(order.getTaxAmount())
                                                .shippingCost(order.getShippingCost())
                                                .grandTotal(order.getGrandTotal())
                                                .currency(order.getCurrency())
                                                .exchangeRate(order.getExchangeRate())
                                                .taxType(order.getTaxType())
                                                .taxRate(order.getTaxRate())
                                                .build())
                                .shippingAddress(AddressInfoResponse.builder()
                                                .address(order.getShippingAddress())
                                                .city(order.getShippingCity())
                                                .state(order.getShippingState())
                                                .postalCode(order.getShippingPostalCode())
                                                .country(order.getShippingCountry())
                                                .build())
                                .billingAddress(AddressInfoResponse.builder()
                                                .address(order.getBillingAddress())
                                                .city(order.getBillingCity())
                                                .state(order.getBillingState())
                                                .postalCode(order.getBillingPostalCode())
                                                .country(order.getBillingCountry())
                                                .build())
                                .customerNotes(order.getCustomerNotes())
                                .internalNotes(order.getInternalNotes())
                                .items(
                                                order.getItems() == null
                                                                ? List.of()
                                                                : order.getItems()
                                                                                .stream()
                                                                                .map(orderItemMapper::toResponse) // .map(orderItemMapper::toResponse)
                                                                                .toList())
                                .createdAt(order.getCreatedAt())
                                .updatedAt(order.getUpdatedAt())

                                .build();
        }
}
