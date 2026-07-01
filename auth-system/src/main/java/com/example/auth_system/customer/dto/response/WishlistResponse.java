package com.example.auth_system.customer.dto.response;

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
public class WishlistResponse {

    private UUID id;
    private String name;
    private UUID customerId;
    private String customerName;
    private int itemCount;
    private List<WishlistItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishlistItemResponse {
        private UUID id;
        private UUID productId;
        private String productName;
        private String productCode;
        private String productSku;
        private Double productPrice;
        private UUID variantId;
        private String variantSku;
        private String variantAttributes;
        private LocalDateTime addedAt;
        private String notes;
    }
}