package com.example.auth_system.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGroupResponse {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal discountPercentage;
    private Boolean isActive;
    private Integer customerCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}