package com.example.auth_system.customer.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWishlistRequest {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
}