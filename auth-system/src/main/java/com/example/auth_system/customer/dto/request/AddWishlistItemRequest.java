package com.example.auth_system.customer.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddWishlistItemRequest {

    @NotNull(message = "Wishlist ID is required")
    private UUID wishlistId;

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID variantId;  // Optional

    private String notes;
}