package com.example.auth_system.order.order.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.order.order.dto.orderRequest.CreateOrderItemRequest;
import com.example.auth_system.order.order.dto.orderResponse.OrderItemResponse;
import com.example.auth_system.order.order.entity.OrderItem;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;

@Component
public class OrderItemMapper {

        // request DTO -> Entity

        public OrderItem toEntity(CreateOrderItemRequest request, Product product, ProductVariant variant) {

                return OrderItem.builder()
                                .product(product)
                                .variant(variant)
                                .quantity(request.getQuantity())
                                // .unitPrice(request.getUnitPrice())
                                .build();

        }

        // Entity -> Response DTO
        public OrderItemResponse toResponse(OrderItem item) {

                if (item == null) {
                        return null;
                }

                return OrderItemResponse.builder()
                                .id(item.getId())
                                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                                .productName(
                                                item.getProduct() != null
                                                                ? item.getProduct().getName()
                                                                : null)

                                .productCode(
                                                item.getProduct() != null
                                                                ? item.getProduct().getProductCode()
                                                                : null)
                                .variantId(
                                                item.getVariant() != null
                                                                ? item.getVariant().getId()
                                                                : null)

                                .variantSku(
                                                item.getVariant() != null
                                                                ? item.getVariant().getSku()
                                                                : null)

                                .variantAttributes(
                                                item.getVariant() != null
                                                                ? item.getVariant().getAttributeValues()
                                                                : null)

                                // Price information
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .discountAmount(item.getDiscountAmount())
                                .taxAmount(item.getTaxAmount())
                                .totalPrice(item.getTotalPrice())

                                // Refund information
                                .refundedQuantity(item.getRefundedQuantity())
                                .isRefunded(item.getIsRefunded())

                                .build();
        }
}