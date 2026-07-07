package com.example.auth_system.order.dto.response.orderResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfoResponse {
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
