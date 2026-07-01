package com.example.auth_system.customer.repository;

import com.example.auth_system.customer.entity.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, UUID> {

    Optional<CustomerGroup> findByName(String name);

    List<CustomerGroup> findByIsActiveTrue();

    boolean existsByName(String name);

    @Query("SELECT cg, COUNT(c) FROM CustomerGroup cg LEFT JOIN cg.customers c GROUP BY cg.id")
    List<Object[]> findCustomerGroupsWithCount();
}