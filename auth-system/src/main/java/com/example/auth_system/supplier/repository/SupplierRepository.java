package com.example.auth_system.supplier.repository;

import com.example.auth_system.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Optional<Supplier> findBySupplierCode(String supplierCode);

    List<Supplier> findByNameContainingIgnoreCase(String name);

    List<Supplier> findByContactPersonContainingIgnoreCase(String contactPerson);

    Optional<Supplier> findByPhone(String phone);

    Optional<Supplier> findByEmail(String email);

    List<Supplier> findByIsActiveTrue();

    List<Supplier> findByIsActiveFalse();

    boolean existsBySupplierCode(String supplierCode);

    boolean existsByName(String name);

    @Query("SELECT s FROM Supplier s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Supplier> searchSuppliers(@Param("searchTerm") String searchTerm);

    long countByIsActiveTrue();

    @Query("SELECT s FROM Supplier s " +
            "JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
            "WHERE ps.product.id = :productId")
    List<Supplier> findSuppliersByProductId(@Param("productId") UUID productId);

    @Query("SELECT s FROM Supplier s " +
            "JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
            "WHERE ps.product.id = :productId AND ps.isPrimary = true")
    Optional<Supplier> findPrimarySupplierByProductId(@Param("productId") UUID productId);
}