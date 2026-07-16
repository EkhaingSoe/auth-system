package com.example.auth_system.order.service;

import com.example.auth_system.order.dto.request.Address.CreateShipmentRequest;
import com.example.auth_system.order.dto.request.Address.UpdateOrderShippingRequest;
import com.example.auth_system.order.dto.response.ShippingResponse.ShipmentResponse;
import com.example.auth_system.order.enums.ShipmentStatus;

import java.util.List;
import java.util.UUID;

public interface ShipmentService {

    ShipmentResponse createShipment(UUID orderId, CreateShipmentRequest request);

    ShipmentResponse getShipmentById(UUID shipmentId);

    List<ShipmentResponse> getShipmentsByOrder(UUID orderId);

    ShipmentResponse updateShipment(UUID shipmentId, UpdateOrderShippingRequest request);

    ShipmentResponse updateShipmentStatus(UUID shipmentId, ShipmentStatus status);

    void deleteShipment(UUID shipmentId);
}