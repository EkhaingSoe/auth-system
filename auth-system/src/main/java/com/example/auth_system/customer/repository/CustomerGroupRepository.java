package com.example.auth_system.customer.repository;

import com.example.auth_system.customer.entity.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, UUID> {

    Optional<CustomerGroup> findByName(String name);

    Page<CustomerGroup> findByIsActiveTrue(Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT cg, COUNT(c) FROM CustomerGroup cg LEFT JOIN cg.customers c GROUP BY cg.id")
    Page<Object[]> findCustomerGroupsWithCount(Pageable pageable);
}