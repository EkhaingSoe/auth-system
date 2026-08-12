// src/main/java/com/example/auth_system/brand/repository/BrandRepository.java
package com.example.auth_system.brand.repository;

import com.example.auth_system.brand.entity.Brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Optional<Brand> findByName(String name);

    boolean existsByName(String name);

    Page<Brand> findByIsActiveTrue(Pageable pageable);

    List<Brand> findByIsActiveFalse();

    @Query("SELECT b FROM Brand b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Brand> searchBrands(@Param("searchTerm") String searchTerm, Pageable pageable);
}