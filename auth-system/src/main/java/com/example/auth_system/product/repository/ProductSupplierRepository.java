package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, UUID> {

    List<ProductSupplier> findByProductId(UUID productId);

    List<ProductSupplier> findBySupplierId(UUID supplierId);

    Optional<ProductSupplier> findByProductIdAndIsPrimaryTrue(UUID productId);

    Optional<ProductSupplier> findByProductIdAndSupplierId(UUID productId, UUID supplierId);

    List<ProductSupplier> findByProductIdAndIsPrimary(UUID productId, Boolean isPrimary);

    boolean existsByProductIdAndSupplierId(UUID productId, UUID supplierId);
}