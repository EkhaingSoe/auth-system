package com.example.auth_system.order.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.order.dto.request.RefundItemRequest;
import com.example.auth_system.order.entity.OrderItem;
import com.example.auth_system.order.entity.RefundItem;

import java.math.BigDecimal;

@Component
public class RefundItemMapper {

    public RefundItem toEntity(OrderItem orderItem, RefundItemRequest request) {

        return RefundItem.builder()
                .orderItemId(orderItem.getId())

                .productId(
                        orderItem.getProduct() != null
                                ? orderItem.getProduct().getId()
                                : null)

                .variantId(
                        orderItem.getVariant() != null
                                ? orderItem.getVariant().getId()
                                : null)

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
                                        BigDecimal.valueOf(request.getQuantity())))

                .build();
    }
}