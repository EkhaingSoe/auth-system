package com.example.auth_system.customer.service;

import com.example.auth_system.customer.dto.request.CreateCustomerRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerRequest;
import com.example.auth_system.customer.dto.response.CustomerResponse;

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

    List<CustomerResponse> getAllCustomers();

    List<CustomerResponse> getActiveCustomers();

    List<CustomerResponse> getVipCustomers();

    List<CustomerResponse> searchCustomers(String term);

    void deleteCustomer(UUID id);

    void activateCustomer(UUID id);

    void deactivateCustomer(UUID id);

    long countActiveCustomers();

    List<CustomerResponse> getCustomersByGroupId(UUID groupId);

    void updateCustomerSpending(UUID customerId, BigDecimal amount);
}