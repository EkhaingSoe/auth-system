package com.example.auth_system.customer.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.customer.dto.request.CreateCustomerGroupRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerGroupRequest;
import com.example.auth_system.customer.dto.response.CustomerGroupResponse;
import com.example.auth_system.customer.entity.CustomerGroup;
import com.example.auth_system.customer.mapper.CustomerGroupMapper;
import com.example.auth_system.customer.repository.CustomerGroupRepository;
import com.example.auth_system.customer.service.CustomerGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerGroupServiceImpl implements CustomerGroupService {

    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerGroupMapper customerGroupMapper;

    // ============================================================
    // CREATE CUSTOMER GROUP
    // ============================================================

    @Override
    public CustomerGroupResponse createCustomerGroup(CreateCustomerGroupRequest request) {
        log.info("Creating customer group: {}", request.getName());

        // Validate name uniqueness
        if (customerGroupRepository.existsByName(request.getName())) {
            throw new BusinessException("Customer group with name already exists: " + request.getName());
        }

        CustomerGroup group = customerGroupMapper.toEntity(request);
        group = customerGroupRepository.save(group);

        log.info("Customer group created successfully with id: {}", group.getId());
        return customerGroupMapper.toResponse(group);
    }

    // ============================================================
    // UPDATE CUSTOMER GROUP
    // ============================================================

    @Override
    public CustomerGroupResponse updateCustomerGroup(UUID id, UpdateCustomerGroupRequest request) {
        log.info("Updating customer group: {}", id);

        CustomerGroup group = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));

        // Validate name uniqueness if changed
        if (request.getName() != null && !group.getName().equals(request.getName())
                && customerGroupRepository.existsByName(request.getName())) {
            throw new BusinessException("Customer group with name already exists: " + request.getName());
        }

        customerGroupMapper.updateEntity(group, request);
        group = customerGroupRepository.save(group);

        log.info("Customer group updated successfully: {}", id);
        return customerGroupMapper.toResponse(group);
    }

    // ============================================================
    // GET GROUP BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerGroupResponse getCustomerGroupById(UUID id) {
        log.info("Getting customer group by id: {}", id);
        CustomerGroup group = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));
        return customerGroupMapper.toResponse(group);
    }

    // ============================================================
    // GET GROUP BY NAME
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerGroupResponse getCustomerGroupByName(String name) {
        log.info("Getting customer group by name: {}", name);
        CustomerGroup group = customerGroupRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with name: " + name));
        return customerGroupMapper.toResponse(group);
    }

    // ============================================================
    // GET ALL GROUPS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CustomerGroupResponse> getAllCustomerGroups() {
        log.info("Getting all customer groups");
        return customerGroupRepository.findAll().stream()
                .map(customerGroupMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET ACTIVE GROUPS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CustomerGroupResponse> getActiveCustomerGroups() {
        log.info("Getting active customer groups");
        return customerGroupRepository.findByIsActiveTrue().stream()
                .map(customerGroupMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ============================================================
    // DELETE GROUP (Soft Delete)
    // ============================================================

    @Override
    public void deleteCustomerGroup(UUID id) {
        log.info("Deleting customer group: {}", id);
        CustomerGroup group = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));

        // Check if group has customers
        if (group.getCustomers() != null && !group.getCustomers().isEmpty()) {
            throw new BusinessException("Cannot delete group with associated customers. Reassign customers first.");
        }

        group.setIsActive(false);
        customerGroupRepository.save(group);
        log.info("Customer group deactivated successfully: {}", id);
    }

    // ============================================================
    // ACTIVATE GROUP
    // ============================================================

    @Override
    public void activateCustomerGroup(UUID id) {
        log.info("Activating customer group: {}", id);
        CustomerGroup group = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));
        group.setIsActive(true);
        customerGroupRepository.save(group);
        log.info("Customer group activated successfully: {}", id);
    }

    // ============================================================
    // DEACTIVATE GROUP
    // ============================================================

    @Override
    public void deactivateCustomerGroup(UUID id) {
        log.info("Deactivating customer group: {}", id);
        CustomerGroup group = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));
        group.setIsActive(false);
        customerGroupRepository.save(group);
        log.info("Customer group deactivated successfully: {}", id);
    }

    // ============================================================
    // GET GROUPS WITH CUSTOMER COUNT
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CustomerGroupResponse> getCustomerGroupsWithCount() {
        log.info("Getting customer groups with customer count");
        
        List<Object[]> results = customerGroupRepository.findCustomerGroupsWithCount();
        
        return results.stream()
                .map(result -> {
                    CustomerGroup group = (CustomerGroup) result[0];
                    Long count = (Long) result[1];
                    CustomerGroupResponse response = customerGroupMapper.toResponse(group);
                    response.setCustomerCount(count.intValue());
                    return response;
                })
                .collect(Collectors.toList());
    }
}