package com.example.auth_system.customer.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.customer.dto.request.AddWishlistItemRequest;
import com.example.auth_system.customer.dto.request.CreateWishlistRequest;
import com.example.auth_system.customer.dto.request.UpdateWishlistRequest;
import com.example.auth_system.customer.dto.response.WishlistResponse;
import com.example.auth_system.customer.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/wishlists")
@RequiredArgsConstructor
@Slf4j
public class WishlistController {

    private final WishlistService wishlistService;

    // ============================================================
    // WISHLIST (PARENT) OPERATIONS
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<WishlistResponse>> createWishlist(
            @Valid @RequestBody CreateWishlistRequest request) {
        log.info("POST /api/customer/wishlists - Creating wishlist");
        WishlistResponse response = wishlistService.createWishlist(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Wishlist created successfully", response));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<WishlistResponse>>> getWishlistsByCustomer(@PathVariable UUID customerId) {
        log.info("GET /api/customer/wishlists/customer/{} - Getting wishlists", customerId);
        List<WishlistResponse> wishlists = wishlistService.getWishlistsByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlists retrieved", wishlists));
    }

    @GetMapping("/{wishlistId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<WishlistResponse>> getWishlistById(@PathVariable UUID wishlistId) {
        log.info("GET /api/customer/wishlists/{} - Getting wishlist", wishlistId);
        WishlistResponse wishlist = wishlistService.getWishlistById(wishlistId);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlist retrieved", wishlist));
    }

    @GetMapping("/{wishlistId}/customer/{customerId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<WishlistResponse>> getWishlistByCustomer(
            @PathVariable UUID wishlistId,
            @PathVariable UUID customerId) {
        log.info("GET /api/customer/wishlists/{}/customer/{} - Getting wishlist", wishlistId, customerId);
        WishlistResponse wishlist = wishlistService.getWishlistByIdAndCustomer(wishlistId, customerId);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlist retrieved", wishlist));
    }

    @PutMapping("/{wishlistId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<WishlistResponse>> updateWishlist(
            @PathVariable UUID wishlistId,
            @Valid @RequestBody UpdateWishlistRequest request) {
        log.info("PUT /api/customer/wishlists/{} - Updating wishlist", wishlistId);
        WishlistResponse wishlist = wishlistService.updateWishlist(wishlistId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlist updated", wishlist));
    }

    @DeleteMapping("/{wishlistId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteWishlist(@PathVariable UUID wishlistId) {
        log.info("DELETE /api/customer/wishlists/{} - Deleting wishlist", wishlistId);
        wishlistService.deleteWishlist(wishlistId);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlist deleted", null));
    }

    @DeleteMapping("/{wishlistId}/customer/{customerId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteWishlistByCustomer(
            @PathVariable UUID wishlistId,
            @PathVariable UUID customerId) {
        log.info("DELETE /api/customer/wishlists/{}/customer/{} - Deleting wishlist", wishlistId, customerId);
        wishlistService.deleteWishlistByIdAndCustomer(wishlistId, customerId);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlist deleted", null));
    }

    // ============================================================
    // WISHLIST ITEM OPERATIONS
    // ============================================================

    @PostMapping("/items")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<WishlistResponse>> addItemToWishlist(
            @Valid @RequestBody AddWishlistItemRequest request) {
        log.info("POST /api/customer/wishlists/items - Adding item to wishlist");
        WishlistResponse wishlist = wishlistService.addItemToWishlist(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Item added to wishlist", wishlist));
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<Void>> removeItemFromWishlist(@PathVariable UUID itemId) {
        log.info("DELETE /api/customer/wishlists/items/{} - Removing item", itemId);
        wishlistService.removeItemFromWishlist(itemId);
        return ResponseEntity.ok(ApiResponse.success(200, "Item removed from wishlist", null));
    }

    @DeleteMapping("/{wishlistId}/products/{productId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<Void>> removeProductFromWishlist(
            @PathVariable UUID wishlistId,
            @PathVariable UUID productId) {
        log.info("DELETE /api/customer/wishlists/{}/products/{} - Removing product", wishlistId, productId);
        wishlistService.removeItemFromWishlist(wishlistId, productId);
        return ResponseEntity.ok(ApiResponse.success(200, "Product removed from wishlist", null));
    }

    @DeleteMapping("/{wishlistId}/clear")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<Void>> clearWishlist(@PathVariable UUID wishlistId) {
        log.info("DELETE /api/customer/wishlists/{}/clear - Clearing wishlist", wishlistId);
        wishlistService.clearWishlist(wishlistId);
        return ResponseEntity.ok(ApiResponse.success(200, "Wishlist cleared", null));
    }

    @GetMapping("/{wishlistId}/check")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<Boolean>> checkProductInWishlist(
            @PathVariable UUID wishlistId,
            @RequestParam UUID productId,
            @RequestParam(required = false) UUID variantId) {
        log.info("GET /api/customer/wishlists/{}/check - Checking product in wishlist", wishlistId);
        boolean exists = wishlistService.isProductInWishlist(wishlistId, productId, variantId);
        return ResponseEntity.ok(ApiResponse.success(200, "Check completed", exists));
    }

    @GetMapping("/{wishlistId}/count")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<Integer>> getWishlistItemCount(@PathVariable UUID wishlistId) {
        log.info("GET /api/customer/wishlists/{}/count - Getting item count", wishlistId);
        int count = wishlistService.getWishlistItemCount(wishlistId);
        return ResponseEntity.ok(ApiResponse.success(200, "Item count retrieved", count));
    }
}