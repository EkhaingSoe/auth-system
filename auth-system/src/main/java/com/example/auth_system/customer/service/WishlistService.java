package com.example.auth_system.customer.service;

import com.example.auth_system.customer.dto.request.AddWishlistItemRequest;
import com.example.auth_system.customer.dto.request.CreateWishlistRequest;
import com.example.auth_system.customer.dto.request.UpdateWishlistRequest;
import com.example.auth_system.customer.dto.response.WishlistResponse;

import java.util.List;
import java.util.UUID;

public interface WishlistService {

    // Wishlist (Parent)
    WishlistResponse createWishlist(CreateWishlistRequest request);

    WishlistResponse updateWishlist(UUID wishlistId, UpdateWishlistRequest request);

    WishlistResponse getWishlistById(UUID wishlistId);

    WishlistResponse getWishlistByIdAndCustomer(UUID wishlistId, UUID customerId);

    List<WishlistResponse> getWishlistsByCustomerId(UUID customerId);

    void deleteWishlist(UUID wishlistId);

    void deleteWishlistByIdAndCustomer(UUID wishlistId, UUID customerId);

    // Wishlist Items
    WishlistResponse addItemToWishlist(AddWishlistItemRequest request);

    void removeItemFromWishlist(UUID itemId);

    void removeItemFromWishlist(UUID wishlistId, UUID productId);

    void clearWishlist(UUID wishlistId);

    boolean isProductInWishlist(UUID wishlistId, UUID productId, UUID variantId);

    int getWishlistItemCount(UUID wishlistId);
}