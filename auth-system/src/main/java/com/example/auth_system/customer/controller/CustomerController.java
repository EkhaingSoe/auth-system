package com.example.auth_system.customer.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.customer.dto.request.CreateCustomerRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerRequest;
import com.example.auth_system.customer.dto.response.CustomerResponse;
import com.example.auth_system.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    // ============================================================
    // CRUD OPERATIONS
    // ============================================================

    @PostMapping
    @PreAuthorize("@permission.hasPermission('CUSTOMER_CREATE')")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        log.info("POST /api/admin/customers - Creating customer: {} {}", 
                request.getFirstName(), request.getLastName());
        CustomerResponse customer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Customer created successfully", customer));
    }

    @GetMapping
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        log.info("GET /api/admin/customers - Getting all customers");
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(200, "Customers retrieved successfully", customers));
    }

    @GetMapping("/active")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getActiveCustomers() {
        log.info("GET /api/admin/customers/active - Getting active customers");
        List<CustomerResponse> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(ApiResponse.success(200, "Active customers retrieved successfully", customers));
    }

    @GetMapping("/vip")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getVipCustomers() {
        log.info("GET /api/admin/customers/vip - Getting VIP customers");
        List<CustomerResponse> customers = customerService.getVipCustomers();
        return ResponseEntity.ok(ApiResponse.success(200, "VIP customers retrieved successfully", customers));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable UUID id) {
        log.info("GET /api/admin/customers/{} - Getting customer by id", id);
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer retrieved successfully", customer));
    }

    @GetMapping("/code/{customerCode}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByCode(@PathVariable String customerCode) {
        log.info("GET /api/admin/customers/code/{} - Getting customer by code", customerCode);
        CustomerResponse customer = customerService.getCustomerByCode(customerCode);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer retrieved successfully", customer));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByEmail(@PathVariable String email) {
        log.info("GET /api/admin/customers/email/{} - Getting customer by email", email);
        CustomerResponse customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer retrieved successfully", customer));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByUserId(@PathVariable UUID userId) {
        log.info("GET /api/admin/customers/user/{} - Getting customer by user id", userId);
        CustomerResponse customer = customerService.getCustomerByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer retrieved successfully", customer));
    }

    @GetMapping("/search")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> searchCustomers(@RequestParam String term) {
        log.info("GET /api/admin/customers/search - Searching customers with term: {}", term);
        List<CustomerResponse> customers = customerService.searchCustomers(term);
        return ResponseEntity.ok(ApiResponse.success(200, "Customers found", customers));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        log.info("PUT /api/admin/customers/{} - Updating customer", id);
        CustomerResponse customer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer updated successfully", customer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        log.info("DELETE /api/admin/customers/{} - Deleting customer", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer deactivated successfully", null));
    }

    // ============================================================
    // ACTIVATE / DEACTIVATE
    // ============================================================

    @PatchMapping("/{id}/activate")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<CustomerResponse>> activateCustomer(@PathVariable UUID id) {
        log.info("PATCH /api/admin/customers/{}/activate - Activating customer", id);
        customerService.activateCustomer(id);
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer activated successfully", customer));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<CustomerResponse>> deactivateCustomer(@PathVariable UUID id) {
        log.info("PATCH /api/admin/customers/{}/deactivate - Deactivating customer", id);
        customerService.deactivateCustomer(id);
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer deactivated successfully", customer));
    }

    // ============================================================
    // STATISTICS
    // ============================================================

    @GetMapping("/count/active")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<Long>> countActiveCustomers() {
        log.info("GET /api/admin/customers/count/active - Counting active customers");
        long count = customerService.countActiveCustomers();
        return ResponseEntity.ok(ApiResponse.success(200, "Active customer count retrieved", count));
    }

    // ============================================================
    // GROUP OPERATIONS
    // ============================================================

    @GetMapping("/group/{groupId}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getCustomersByGroup(@PathVariable UUID groupId) {
        log.info("GET /api/admin/customers/group/{} - Getting customers by group", groupId);
        List<CustomerResponse> customers = customerService.getCustomersByGroupId(groupId);
        return ResponseEntity.ok(ApiResponse.success(200, "Customers by group retrieved", customers));
    }
}