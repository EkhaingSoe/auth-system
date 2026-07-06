package com.example.auth_system.order.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderShippingRequest {

    private String shippingMethod;
    private String shippingTrackingNumber;

    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;
}