package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByProductCode(String productCode);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByIsActiveTrue();

    List<Product> findByCategoryId(UUID categoryId);

    List<Product> findByBrandId(UUID brandId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Product> searchProducts(@Param("term") String term);

    @Query("SELECT p FROM Product p JOIN p.variants v WHERE v.sku = :sku")
    Optional<Product> findByVariantSku(@Param("sku") String sku);

    @Query("SELECT p FROM Product p JOIN p.variants v WHERE v.barcode = :barcode")
    Optional<Product> findByVariantBarcode(@Param("barcode") String barcode);

    boolean existsByProductCode(String productCode);

    boolean existsByName(String name);
}