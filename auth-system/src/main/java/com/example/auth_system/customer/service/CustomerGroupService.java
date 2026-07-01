package com.example.auth_system.customer.service;

import com.example.auth_system.customer.dto.request.CreateCustomerGroupRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerGroupRequest;
import com.example.auth_system.customer.dto.response.CustomerGroupResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerGroupService {

    // CRUD Operations
    CustomerGroupResponse createCustomerGroup(CreateCustomerGroupRequest request);
    CustomerGroupResponse updateCustomerGroup(UUID id, UpdateCustomerGroupRequest request);
    CustomerGroupResponse getCustomerGroupById(UUID id);
    CustomerGroupResponse getCustomerGroupByName(String name);
    List<CustomerGroupResponse> getAllCustomerGroups();
    List<CustomerGroupResponse> getActiveCustomerGroups();
    void deleteCustomerGroup(UUID id);

    // Status Operations
    void activateCustomerGroup(UUID id);
    void deactivateCustomerGroup(UUID id);

    // Statistics
    List<CustomerGroupResponse> getCustomerGroupsWithCount();
}