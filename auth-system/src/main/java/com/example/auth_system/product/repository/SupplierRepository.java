// package com.example.auth_system.product.repository;

// import com.example.auth_system.product.entity.Supplier;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

// /**
// * Find supplier by supplier code (auto-generated: SUP0001, SUP0002, etc.)
// */
// Optional<Supplier> findBySupplierCode(String supplierCode);

// /**
// * Find suppliers by name (case insensitive, partial match)
// */
// List<Supplier> findByNameContainingIgnoreCase(String name);

// /**
// * Find suppliers by contact person name
// */
// List<Supplier> findByContactPersonContainingIgnoreCase(String contactPerson);

// /**
// * Find suppliers by phone number
// */
// Optional<Supplier> findByPhone(String phone);

// /**
// * Find suppliers by email
// */
// Optional<Supplier> findByEmail(String email);

// /**
// * Get all active suppliers
// */
// List<Supplier> findByIsActiveTrue();

// /**
// * Get all inactive suppliers
// */
// List<Supplier> findByIsActiveFalse();

// /**
// * Check if supplier code exists
// */
// boolean existsBySupplierCode(String supplierCode);

// /**
// * Check if supplier name exists (for duplicate validation)
// */
// boolean existsByName(String name);

// /**
// * Search suppliers by name or contact person (for autocomplete)
// */
// @Query("SELECT s FROM Supplier s WHERE " +
// "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
// "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
// "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
// List<Supplier> searchSuppliers(@Param("searchTerm") String searchTerm);

// /**
// * Count active suppliers
// */
// long countByIsActiveTrue();

// /**
// * Get suppliers with their product count (for reporting)
// */
// @Query("SELECT s, COUNT(ps) as productCount FROM Supplier s " +
// "LEFT JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
// "GROUP BY s.id " +
// "ORDER BY productCount DESC")
// List<Object[]> findSuppliersWithProductCount();

// /**
// * Find suppliers that supply a specific product
// */
// @Query("SELECT s FROM Supplier s " +
// "JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
// "WHERE ps.product.id = :productId")
// List<Supplier> findSuppliersByProductId(@Param("productId") UUID productId);

// /**
// * Get primary supplier for a product
// */
// @Query("SELECT s FROM Supplier s " +
// "JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
// "WHERE ps.product.id = :productId AND ps.isPrimary = true")
// Optional<Supplier> findPrimarySupplierByProductId(@Param("productId") UUID
// productId);

// /**
// * Find suppliers by lead time (less than or equal to specified days)
// */
// @Query("SELECT DISTINCT s FROM Supplier s " +
// "JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
// "WHERE ps.leadTimeDays <= :maxLeadTime")
// List<Supplier> findSuppliersByMaxLeadTime(@Param("maxLeadTime") Integer
// maxLeadTime);

// /**
// * Get supplier with most products supplied
// */
// @Query("SELECT s FROM Supplier s " +
// "JOIN ProductSupplier ps ON ps.supplier.id = s.id " +
// "GROUP BY s.id " +
// "ORDER BY COUNT(ps.id) DESC " +
// "LIMIT 1")
// Optional<Supplier> findTopSupplier();

// /**
// * Search suppliers with pagination support (conceptual - add Pageable if
// * needed)
// */
// @Query("SELECT s FROM Supplier s WHERE " +
// "(:searchTerm IS NULL OR " +
// "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
// "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
// "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
// "AND (:isActive IS NULL OR s.isActive = :isActive)")
// List<Supplier> findSuppliersWithFilters(
// @Param("searchTerm") String searchTerm,
// @Param("isActive") Boolean isActive);
// }