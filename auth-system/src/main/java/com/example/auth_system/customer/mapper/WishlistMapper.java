package com.example.auth_system.customer.mapper;

import com.example.auth_system.customer.dto.request.CreateWishlistRequest;
import com.example.auth_system.customer.dto.response.WishlistResponse;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.customer.entity.Wishlist;
import com.example.auth_system.customer.entity.WishlistItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WishlistMapper {

    private final ObjectMapper objectMapper;

    public Wishlist toEntity(Customer customer, CreateWishlistRequest request) {
        return Wishlist.builder()
                .customer(customer)
                .name(request.getName())
                .build();
    }

    public WishlistResponse toResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        return WishlistResponse.builder()
                .id(wishlist.getId())
                .name(wishlist.getName())
                .customerId(wishlist.getCustomer() != null ? wishlist.getCustomer().getId() : null)
                .customerName(wishlist.getCustomer() != null ? wishlist.getCustomer().getFullName() : null)
                .itemCount(wishlist.getItemCount())
                .items(wishlist.getItems() != null ?
                        wishlist.getItems().stream()
                                .map(this::toItemResponse)
                                .collect(Collectors.toList()) : null)
                .createdAt(wishlist.getCreatedAt())
                .updatedAt(wishlist.getUpdatedAt())
                .build();
    }

    public WishlistResponse.WishlistItemResponse toItemResponse(WishlistItem item) {
        if (item == null) {
            return null;
        }

        String variantAttributes = null;
        if (item.getVariant() != null && item.getVariant().getAttributeValues() != null) {
            try {
                variantAttributes = objectMapper.writeValueAsString(item.getVariant().getAttributeValues());
            } catch (Exception e) {
                variantAttributes = item.getVariant().getAttributeValues().toString();
            }
        }

        return WishlistResponse.WishlistItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .productCode(item.getProduct() != null ? item.getProduct().getProductCode() : null)
                .productSku(item.getVariant() != null ? item.getVariant().getSku() : 
                            (item.getProduct() != null ? item.getProduct().getProductCode() : null))
                .productPrice(item.getVariant() != null && item.getVariant().getSellingPrice() != null ?
                            item.getVariant().getSellingPrice().doubleValue() :
                            (item.getProduct() != null ? item.getProduct().getMinPrice().doubleValue() : 0.0))
                .variantId(item.getVariant() != null ? item.getVariant().getId() : null)
                .variantSku(item.getVariant() != null ? item.getVariant().getSku() : null)
                .variantAttributes(variantAttributes)
                .addedAt(item.getAddedAt())
                .notes(item.getNotes())
                .build();
    }
}