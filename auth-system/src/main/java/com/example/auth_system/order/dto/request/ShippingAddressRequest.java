package com.example.auth_system.order.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressRequest {

    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}