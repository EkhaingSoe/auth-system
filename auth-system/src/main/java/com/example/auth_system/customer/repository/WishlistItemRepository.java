package com.example.auth_system.customer.repository;

import com.example.auth_system.customer.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, UUID> {

    List<WishlistItem> findByWishlistId(UUID wishlistId);

    List<WishlistItem> findByWishlistIdOrderByAddedAtDesc(UUID wishlistId);

    Optional<WishlistItem> findByWishlistIdAndProductIdAndVariantId(UUID wishlistId, UUID productId, UUID variantId);

    @Query("SELECT wi FROM WishlistItem wi JOIN FETCH wi.product WHERE wi.wishlist.id = :wishlistId")
    List<WishlistItem> findWithProductByWishlistId(@Param("wishlistId") UUID wishlistId);

    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId AND wi.product.id = :productId")
    void deleteByWishlistIdAndProductId(@Param("wishlistId") UUID wishlistId, @Param("productId") UUID productId);

    long countByWishlistId(UUID wishlistId);

    boolean existsByWishlistIdAndProductIdAndVariantId(UUID wishlistId, UUID productId, UUID variantId);
}