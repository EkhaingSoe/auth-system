// src/main/java/com/example/auth_system/brand/repository/BrandRepository.java
package com.example.auth_system.brand.repository;

import com.example.auth_system.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Optional<Brand> findByName(String name);

    boolean existsByName(String name);

    List<Brand> findByIsActiveTrue();

    @Query("SELECT b FROM Brand b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Brand> searchBrands(@Param("searchTerm") String searchTerm);
}