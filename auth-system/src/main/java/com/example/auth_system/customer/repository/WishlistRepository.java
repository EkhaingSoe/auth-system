package com.example.auth_system.customer.repository;

import com.example.auth_system.customer.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    List<Wishlist> findByCustomerId(UUID customerId);

    List<Wishlist> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    Optional<Wishlist> findByIdAndCustomerId(UUID id, UUID customerId);

    @Query("SELECT w FROM Wishlist w LEFT JOIN FETCH w.items WHERE w.customer.id = :customerId")
    List<Wishlist> findWithItemsByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT w FROM Wishlist w LEFT JOIN FETCH w.items WHERE w.id = :wishlistId AND w.customer.id = :customerId")
    Optional<Wishlist> findWithItemsByIdAndCustomerId(@Param("wishlistId") UUID wishlistId, @Param("customerId") UUID customerId);

    long countByCustomerId(UUID customerId);

    boolean existsByIdAndCustomerId(UUID id, UUID customerId);
}