package com.example.auth_system.customer.service;

import com.example.auth_system.customer.dto.request.CreateCustomerGroupRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerGroupRequest;
import com.example.auth_system.customer.dto.response.CustomerGroupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CustomerGroupService {

    // CRUD Operations
    CustomerGroupResponse createCustomerGroup(CreateCustomerGroupRequest request);

    CustomerGroupResponse updateCustomerGroup(UUID id, UpdateCustomerGroupRequest request);

    CustomerGroupResponse getCustomerGroupById(UUID id);

    CustomerGroupResponse getCustomerGroupByName(String name);

    List<CustomerGroupResponse> getAllCustomerGroups();

    Page<CustomerGroupResponse> getActiveCustomerGroups(Pageable pageable);

    void deleteCustomerGroup(UUID id);

    // Status Operations
    void activateCustomerGroup(UUID id);

    void deactivateCustomerGroup(UUID id);

    // Statistics
    Page<CustomerGroupResponse> getCustomerGroupsWithCount(Pageable pageable);
}