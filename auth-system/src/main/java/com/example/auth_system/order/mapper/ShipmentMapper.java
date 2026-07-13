package com.example.auth_system.order.mapper;

import org.springframework.stereotype.Component;

import com.example.auth_system.order.dto.request.CreateShipmentRequest;
import com.example.auth_system.order.dto.response.ShippingResponse.ShipmentResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.OrderShipment;

@Component
public class ShipmentMapper {

    public OrderShipment toEntity(
            CreateShipmentRequest request,
            Order order) {

        return OrderShipment.builder()
                .order(order)
                .shippingMethod(request.getShippingMethod())
                .trackingNumber(request.getTrackingNumber())
                .carrierName(request.getCarrierName())
                .carrierPhone(request.getCarrierPhone())
                .shippingCost(request.getShippingCost())
                .estimatedDelivery(request.getEstimatedDelivery())
                .status("PENDING")
                .build();
    }

    public ShipmentResponse toResponse(
            OrderShipment shipment) {

        if (shipment == null) {
            return null;
        }

        return ShipmentResponse.builder()
                .id(shipment.getId())

                .orderId(
                        shipment.getOrder() != null
                                ? shipment.getOrder().getId()
                                : null)

                .shippingMethod(
                        shipment.getShippingMethod())

                .trackingNumber(
                        shipment.getTrackingNumber())

                .carrierName(
                        shipment.getCarrierName())

                .carrierPhone(
                        shipment.getCarrierPhone())

                .shippingDate(
                        shipment.getShippingDate())

                .estimatedDelivery(
                        shipment.getEstimatedDelivery())

                .actualDelivery(
                        shipment.getActualDelivery())

                .shippingCost(
                        shipment.getShippingCost())

                .status(
                        shipment.getStatus())

                .createdAt(
                        shipment.getCreatedAt())

                .updatedAt(
                        shipment.getUpdatedAt())

                .build();
    }
}