package com.example.auth_system.order.order.dto.orderRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.auth_system.order.order.enums.OrderType;
import com.example.auth_system.order.shipment.dto.shipmentRequest.BillingAddressRequest;
import com.example.auth_system.order.shipment.dto.shipmentRequest.ShippingAddressRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    private UUID customerId;

    // @NotNull(message = "Created by is required")
    // private UUID createdBy; i will get user from backend, so no need to send this
    // from frontend

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<CreateOrderItemRequest> items;

    @Valid
    private ShippingAddressRequest shippingAddress;

    @Valid
    private BillingAddressRequest billingAddress;

    private String couponCode;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String customerNotes;

}
