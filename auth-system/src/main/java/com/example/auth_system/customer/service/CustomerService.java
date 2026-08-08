package com.example.auth_system.customer.service;

import com.example.auth_system.customer.dto.request.CreateCustomerRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerRequest;
import com.example.auth_system.customer.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request);

    CustomerResponse getCustomerById(UUID id);

    CustomerResponse getCustomerByCode(String customerCode);

    CustomerResponse getCustomerByEmail(String email);

    CustomerResponse getCustomerByUserId(UUID userId);

    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    Page<CustomerResponse> getActiveCustomers(Pageable pageable);

    Page<CustomerResponse> getVipCustomers(Pageable pageable);

    Page<CustomerResponse> searchCustomers(String term, Pageable pageable);

    void deleteCustomer(UUID id);

    void activateCustomer(UUID id);

    void deactivateCustomer(UUID id);

    long countActiveCustomers();

    Page<CustomerResponse> getCustomersByGroupId(UUID groupId, Pageable pageable);

    void updateCustomerSpending(UUID customerId, BigDecimal amount);
}