package com.example.auth_system.customer.repository;

import com.example.auth_system.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByCustomerCode(String customerCode);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByUserId(UUID userId);

    List<Customer> findByIsActiveTrue();

    List<Customer> findByIsVipTrue();

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);

    long countByIsActiveTrue();

    @Query("SELECT c FROM Customer c WHERE c.customerGroup.id = :groupId")
    List<Customer> findByCustomerGroupId(@Param("groupId") UUID groupId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUserId(UUID userId);
}