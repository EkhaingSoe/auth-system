package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // =========================
    // Single product
    // =========================

    Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(String productCode);

    boolean existsByName(String name);

    // List<Product> findByNameContainingIgnoreCase(String name);

    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Product> findByBrandId(UUID brandId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Product> searchProducts(@Param("term") String term, Pageable pageable);

}