package com.example.auth_system.customer.mapper;

import com.example.auth_system.customer.dto.request.CreateCustomerGroupRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerGroupRequest;
import com.example.auth_system.customer.dto.response.CustomerGroupResponse;
import com.example.auth_system.customer.entity.CustomerGroup;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class CustomerGroupMapper {

    public CustomerGroup toEntity(CreateCustomerGroupRequest request) {
        return CustomerGroup.builder()
                .name(request.getName())
                .description(request.getDescription())
                .discountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : BigDecimal.ZERO)
                .isActive(true)
                .build();
    }

    public void updateEntity(CustomerGroup group, UpdateCustomerGroupRequest request) {
        if (request.getName() != null) {
            group.setName(request.getName());
        }
        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }
        if (request.getDiscountPercentage() != null) {
            group.setDiscountPercentage(request.getDiscountPercentage());
        }
        if (request.getIsActive() != null) {
            group.setIsActive(request.getIsActive());
        }
    }

    public CustomerGroupResponse toResponse(CustomerGroup group) {
        if (group == null) {
            return null;
        }

        return CustomerGroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .discountPercentage(group.getDiscountPercentage())
                .isActive(group.getIsActive())
                .customerCount(group.getCustomers() != null ? group.getCustomers().size() : 0)
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }
}