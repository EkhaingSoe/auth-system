package com.example.auth_system.customer.dto.response;

import com.example.auth_system.customer.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private UUID id;
    private String customerCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;

    private UUID userId;
    private String userEmail;

    private UUID customerGroupId;
    private String customerGroupName;
    private BigDecimal discountPercentage;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String companyName;
    private String taxNumber;
    private LocalDate dateOfBirth;
    private Gender gender;

    private BigDecimal totalSpent;
    private Integer loyaltyPoints;
    private Integer orderCount;
    private LocalDateTime lastPurchaseDate;

    private Boolean isActive;
    private Boolean isVip;

    private String notes;

    private List<WishlistResponse> wishlistItems;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}