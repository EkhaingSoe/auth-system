package com.example.auth_system.order.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.order.dto.request.RefundItemRequest;
import com.example.auth_system.order.dto.response.RefundResponse.RefundItemResponse;
import com.example.auth_system.order.entity.RefundItem;
import com.example.auth_system.order.order.entity.OrderItem;

import java.math.BigDecimal;

@Component
public class RefundItemMapper {

        public RefundItem toEntity(OrderItem orderItem, RefundItemRequest request) {

                return RefundItem.builder()
                                .orderItem(orderItem)
                                .product(orderItem.getProduct())
                                .variant(orderItem.getVariant())
                                .productName(orderItem.getProductName())
                                .productSku(orderItem.getProductSku())
                                .variantSku(orderItem.getVariantSku())
                                .variantAttributes(orderItem.getVariantAttributes())
                                .refundQuantity(request.getQuantity())
                                .unitPrice(orderItem.getUnitPrice())
                                .discountAmount(orderItem.getDiscountAmount())
                                .taxAmount(orderItem.getTaxAmount())
                                .refundAmount(
                                                orderItem.getUnitPrice()
                                                                .multiply(
                                                                                BigDecimal.valueOf(
                                                                                                request.getQuantity())))
                                .build();
        }

        public RefundItemResponse toResponse(RefundItem refundItem) {
                return RefundItemResponse.builder()
                                .id(refundItem.getId())
                                .orderItemId(refundItem.getOrderItem() != null
                                                ? refundItem.getOrderItem().getId()
                                                : null)
                                .productId(refundItem.getProduct() != null
                                                ? refundItem.getProduct().getId()
                                                : null)
                                .productName(refundItem.getProductName())
                                .productSku(refundItem.getProductSku())
                                .variantId(refundItem.getVariant() != null
                                                ? refundItem.getVariant().getId()
                                                : null)
                                .variantSku(refundItem.getVariantSku())
                                .variantAttributes(refundItem.getVariantAttributes())
                                .refundQuantity(refundItem.getRefundQuantity())
                                .unitPrice(refundItem.getUnitPrice())
                                .discountAmount(refundItem.getDiscountAmount())
                                .taxAmount(refundItem.getTaxAmount())
                                .refundAmount(refundItem.getRefundAmount())
                                .refundReason(refundItem.getRefundReason())
                                .createdAt(refundItem.getCreatedAt())
                                .build();
        }
}