package com.example.auth_system.customer.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.customer.dto.request.CreateCustomerGroupRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerGroupRequest;
import com.example.auth_system.customer.dto.response.CustomerGroupResponse;
import com.example.auth_system.customer.service.CustomerGroupService;
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
@RequestMapping("/api/admin/customer-groups")
@RequiredArgsConstructor
@Slf4j
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    @PostMapping
    @PreAuthorize("@permission.hasPermission('CUSTOMER_CREATE')")
    public ResponseEntity<ApiResponse<CustomerGroupResponse>> createCustomerGroup(
            @Valid @RequestBody CreateCustomerGroupRequest request) {
        log.info("POST /api/admin/customer-groups - Creating group: {}", request.getName());
        CustomerGroupResponse response = customerGroupService.createCustomerGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Customer group created", response));
    }

    @GetMapping
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerGroupResponse>>> getAllCustomerGroups() {
        log.info("GET /api/admin/customer-groups - Getting all groups");
        List<CustomerGroupResponse> groups = customerGroupService.getAllCustomerGroups();
        return ResponseEntity.ok(ApiResponse.success(200, "Groups retrieved", groups));
    }

    @GetMapping("/active")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerGroupResponse>>> getActiveCustomerGroups() {
        log.info("GET /api/admin/customer-groups/active - Getting active groups");
        List<CustomerGroupResponse> groups = customerGroupService.getActiveCustomerGroups();
        return ResponseEntity.ok(ApiResponse.success(200, "Active groups retrieved", groups));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<CustomerGroupResponse>> getCustomerGroupById(@PathVariable UUID id) {
        log.info("GET /api/admin/customer-groups/{} - Getting group by id", id);
        CustomerGroupResponse group = customerGroupService.getCustomerGroupById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Group retrieved", group));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<CustomerGroupResponse>> getCustomerGroupByName(@PathVariable String name) {
        log.info("GET /api/admin/customer-groups/name/{} - Getting group by name", name);
        CustomerGroupResponse group = customerGroupService.getCustomerGroupByName(name);
        return ResponseEntity.ok(ApiResponse.success(200, "Group retrieved", group));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<CustomerGroupResponse>> updateCustomerGroup(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerGroupRequest request) {
        log.info("PUT /api/admin/customer-groups/{} - Updating group", id);
        CustomerGroupResponse group = customerGroupService.updateCustomerGroup(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Group updated", group));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerGroup(@PathVariable UUID id) {
        log.info("DELETE /api/admin/customer-groups/{} - Deleting group", id);
        customerGroupService.deleteCustomerGroup(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Group deactivated", null));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<CustomerGroupResponse>> activateCustomerGroup(@PathVariable UUID id) {
        log.info("PATCH /api/admin/customer-groups/{}/activate - Activating group", id);
        customerGroupService.activateCustomerGroup(id);
        CustomerGroupResponse group = customerGroupService.getCustomerGroupById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Group activated", group));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_UPDATE')")
    public ResponseEntity<ApiResponse<CustomerGroupResponse>> deactivateCustomerGroup(@PathVariable UUID id) {
        log.info("PATCH /api/admin/customer-groups/{}/deactivate - Deactivating group", id);
        customerGroupService.deactivateCustomerGroup(id);
        CustomerGroupResponse group = customerGroupService.getCustomerGroupById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Group deactivated", group));
    }

    @GetMapping("/with-count")
    @PreAuthorize("@permission.hasPermission('CUSTOMER_READ')")
    public ResponseEntity<ApiResponse<List<CustomerGroupResponse>>> getCustomerGroupsWithCount() {
        log.info("GET /api/admin/customer-groups/with-count - Getting groups with customer count");
        List<CustomerGroupResponse> groups = customerGroupService.getCustomerGroupsWithCount();
        return ResponseEntity.ok(ApiResponse.success(200, "Groups with count retrieved", groups));
    }
}