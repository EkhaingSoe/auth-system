package com.example.auth_system.customer.mapper;

import com.example.auth_system.customer.dto.request.CreateCustomerRequest;
import com.example.auth_system.customer.dto.request.UpdateCustomerRequest;
import com.example.auth_system.customer.dto.response.CustomerResponse;
import com.example.auth_system.customer.dto.response.WishlistResponse;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.customer.entity.CustomerGroup;
import com.example.auth_system.customer.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final WishlistMapper wishlistMapper;

    public Customer toEntity(CreateCustomerRequest request, CustomerGroup customerGroup) {
        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .customerGroup(customerGroup)
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .companyName(request.getCompanyName())
                .taxNumber(request.getTaxNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .notes(request.getNotes())
                .isActive(true)
                .build();
    }

    public void updateEntity(Customer customer, UpdateCustomerRequest request, CustomerGroup customerGroup) {
        if (request.getFirstName() != null) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            customer.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }
        if (customerGroup != null) {
            customer.setCustomerGroup(customerGroup);
        }
        if (request.getAddressLine1() != null) {
            customer.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            customer.setAddressLine2(request.getAddressLine2());
        }
        if (request.getCity() != null) {
            customer.setCity(request.getCity());
        }
        if (request.getState() != null) {
            customer.setState(request.getState());
        }
        if (request.getPostalCode() != null) {
            customer.setPostalCode(request.getPostalCode());
        }
        if (request.getCountry() != null) {
            customer.setCountry(request.getCountry());
        }
        if (request.getCompanyName() != null) {
            customer.setCompanyName(request.getCompanyName());
        }
        if (request.getTaxNumber() != null) {
            customer.setTaxNumber(request.getTaxNumber());
        }
        if (request.getDateOfBirth() != null) {
            customer.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            customer.setGender(request.getGender());
        }
        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }
        if (request.getIsVip() != null) {
            customer.setIsVip(request.getIsVip());
        }
        if (request.getNotes() != null) {
            customer.setNotes(request.getNotes());
        }
    }

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .userId(customer.getUser() != null ? customer.getUser().getId() : null)
                .userEmail(customer.getUser() != null ? customer.getUser().getEmail() : null)
                .customerGroupId(customer.getCustomerGroup() != null ? customer.getCustomerGroup().getId() : null)
                .customerGroupName(customer.getCustomerGroup() != null ? customer.getCustomerGroup().getName() : null)
                .discountPercentage(customer.getCustomerGroup() != null ? customer.getCustomerGroup().getDiscountPercentage() : null)
                .addressLine1(customer.getAddressLine1())
                .addressLine2(customer.getAddressLine2())
                .city(customer.getCity())
                .state(customer.getState())
                .postalCode(customer.getPostalCode())
                .country(customer.getCountry())
                .companyName(customer.getCompanyName())
                .taxNumber(customer.getTaxNumber())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .totalSpent(customer.getTotalSpent())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .orderCount(customer.getOrderCount())
                .lastPurchaseDate(customer.getLastPurchaseDate())
                .isActive(customer.getIsActive())
                .isVip(customer.getIsVip())
                .notes(customer.getNotes())
                .wishlistItems(customer.getWishlistItems() != null ?
                        customer.getWishlistItems().stream()
                                .map(wishlistMapper::toResponse)
                                .collect(Collectors.toList()) : null)
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}