package com.example.auth_system.customer.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.customer.dto.request.CreateCustomerRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerRequest;
import com.example.auth_system.customer.dto.response.CustomerResponse;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.customer.entity.CustomerGroup;
import com.example.auth_system.customer.mapper.CustomerMapper;
import com.example.auth_system.customer.repository.CustomerGroupRepository;
import com.example.auth_system.customer.repository.CustomerRepository;
import com.example.auth_system.customer.service.CustomerService;
import com.example.auth_system.user.entity.User;
import com.example.auth_system.user.repository.UserRepository;
import com.example.auth_system.customer.enums.CustomerStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

    // ============================================================
    // CREATE CUSTOMER
    // ============================================================

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer: {} {}", request.getFirstName(), request.getLastName());

        // Validate email uniqueness
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with email already exists: " + request.getEmail());
        }

        // Validate phone uniqueness
        if (request.getPhone() != null && customerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Customer with phone already exists: " + request.getPhone());
        }

        // Validate user if provided
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            if (customerRepository.existsByUserId(request.getUserId())) {
                throw new BusinessException("User already has a customer profile");
            }
        }

        // Validate customer group if provided
        CustomerGroup customerGroup = null;
        if (request.getCustomerGroupId() != null) {
            customerGroup = customerGroupRepository.findById(request.getCustomerGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Customer group not found with id: " + request.getCustomerGroupId()));
        }

        Customer customer = customerMapper.toEntity(request, customerGroup);
        customer.setUser(user);

        customer = customerRepository.save(customer);
        log.info("Customer created successfully with code: {}", customer.getCustomerCode());

        return customerMapper.toResponse(customer);
    }

    // ============================================================
    // UPDATE CUSTOMER
    // ============================================================

    @Override
    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {
        log.info("Updating customer: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // Validate email uniqueness
        if (request.getEmail() != null && !customer.getEmail().equals(request.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with email already exists: " + request.getEmail());
        }

        // Validate phone uniqueness
        if (request.getPhone() != null && !customer.getPhone().equals(request.getPhone())
                && customerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Customer with phone already exists: " + request.getPhone());
        }

        // Validate customer group if provided
        CustomerGroup customerGroup = null;
        if (request.getCustomerGroupId() != null) {
            customerGroup = customerGroupRepository.findById(request.getCustomerGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Customer group not found with id: " + request.getCustomerGroupId()));
        }

        customerMapper.updateEntity(customer, request, customerGroup);
        customer = customerRepository.save(customer);

        log.info("Customer updated successfully: {}", id);
        return customerMapper.toResponse(customer);
    }

    // ============================================================
    // GET CUSTOMER BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        log.info("Getting customer by id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toResponse(customer);
    }

    // ============================================================
    // GET CUSTOMER BY CODE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCode(String customerCode) {
        log.info("Getting customer by code: {}", customerCode);
        Customer customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with code: " + customerCode));
        return customerMapper.toResponse(customer);
    }

    // ============================================================
    // GET CUSTOMER BY EMAIL
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        log.info("Getting customer by email: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return customerMapper.toResponse(customer);
    }

    // ============================================================
    // GET CUSTOMER BY USER ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByUserId(UUID userId) {
        log.info("Getting customer by user id: {}", userId);
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));
        return customerMapper.toResponse(customer);
    }

    // ============================================================
    // GET ALL CUSTOMERS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        log.info("Getting all customers");
        return customerRepository.findAll(pageable).map(customerMapper::toResponse);
    }

    // ============================================================
    // GET ACTIVE CUSTOMERS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getActiveCustomers(Pageable pageable) {
        log.info("Getting active customers");

        return customerRepository
                .findByStatus(CustomerStatus.ACTIVE, pageable)
                .map(customerMapper::toResponse);
    }

    // ============================================================
    // GET VIP CUSTOMERS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getVipCustomers(Pageable pageable) {
        log.info("Getting VIP customers");
        return customerRepository.findByIsVipTrue(pageable).map(customerMapper::toResponse);
    }

    // ============================================================
    // SEARCH CUSTOMERS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomers(String term, Pageable pageable) {
        log.info("Searching customers with term: {}", term);
        return customerRepository.searchCustomers(term, pageable).map(customerMapper::toResponse);
    }

    // ============================================================
    // DELETE CUSTOMER (Soft Delete)
    // ============================================================

    @Override
    public void deleteCustomer(UUID id) {
        log.info("Deleting customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);
        log.info("Customer deactivated successfully: {}", id);
    }

    // ============================================================
    // ACTIVATE CUSTOMER
    // ============================================================

    @Override
    public void activateCustomer(UUID id) {
        log.info("Activating customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setStatus(CustomerStatus.ACTIVE);
        customerRepository.save(customer);
        log.info("Customer activated successfully: {}", id);
    }

    // ============================================================
    // DEACTIVATE CUSTOMER
    // ============================================================

    @Override
    public void deactivateCustomer(UUID id) {
        log.info("Deactivating customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);
        log.info("Customer deactivated successfully: {}", id);
    }

    // ============================================================
    // COUNT ACTIVE CUSTOMERS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public long countActiveCustomers() {
        log.info("Counting active customers");
        return customerRepository.countByStatus(CustomerStatus.ACTIVE);
    }

    // ============================================================
    // GET CUSTOMERS BY GROUP
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomersByGroupId(UUID groupId, Pageable pageable) {
        log.info("Getting customers by group id: {}", groupId);
        return customerRepository.findByCustomerGroupId(groupId, pageable).map(customerMapper::toResponse);
    }

    // ============================================================
    // UPDATE CUSTOMER SPENDING
    // ============================================================

    @Override
    public void updateCustomerSpending(UUID customerId, BigDecimal amount) {
        log.info("Updating spending for customer: {}, amount: {}", customerId, amount);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        customer.updateSpending(amount);
        customerRepository.save(customer);

        log.info("Customer spending updated successfully");
    }
}