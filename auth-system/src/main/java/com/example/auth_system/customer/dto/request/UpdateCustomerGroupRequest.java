
package com.example.auth_system.customer.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerGroupRequest {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private String description;

    private BigDecimal discountPercentage;

    private Boolean isActive;
}