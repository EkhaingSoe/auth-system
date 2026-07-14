package com.example.auth_system.order.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.order.dto.request.CreateShipmentRequest;
import com.example.auth_system.order.dto.request.UpdateOrderShippingRequest;
import com.example.auth_system.order.dto.response.ShippingResponse.ShipmentResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.OrderShipment;
import com.example.auth_system.order.entity.OrderStatusHistory;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.enums.ShipmentStatus;
import com.example.auth_system.order.mapper.ShipmentMapper;
import com.example.auth_system.order.repository.OrderRepository;
import com.example.auth_system.order.repository.OrderShipmentRepository;
import com.example.auth_system.order.repository.OrderStatusHistoryRepository;
import com.example.auth_system.order.service.OrderStatusService;
import com.example.auth_system.order.service.ShipmentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;
    private final OrderShipmentRepository shipmentRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final UserRepository userRepository;
    private final OrderStatusService orderStatusService;

    @Override
    public ShipmentResponse createShipment(UUID orderId, CreateShipmentRequest request) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found"));
        OrderShipment shipment = shipmentMapper.toEntity(request, order);
        OrderShipment savedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toResponse(savedShipment);

    }

    @Override
    public ShipmentResponse getShipmentById(UUID shipmentId) {

        OrderShipment shipment = shipmentRepository.findById(shipmentId).orElseThrow(
                () -> new ResourceNotFoundException("Shipment not found"));
        return shipmentMapper.toResponse(shipment);
    }

    @Override
    public List<ShipmentResponse> getShipmentsByOrder(UUID orderId) {

        List<OrderShipment> shipments = shipmentRepository.findByOrderId(orderId);
        return shipments.stream().map(shipmentMapper::toResponse).collect(java.util.stream.Collectors.toList());

    }

    @Override
    @Transactional
    public ShipmentResponse updateShipment(UUID shipmentId, UpdateOrderShippingRequest request) {

        OrderShipment shipment = shipmentRepository.findById(shipmentId).orElseThrow(
                () -> new ResourceNotFoundException("Shipment not found"));

        // 2. Update shipment information
        shipment.setShippingMethod(request.getShippingMethod());
        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setCarrierName(request.getCarrierName());
        shipment.setCarrierPhone(request.getCarrierPhone());
        shipment.setShippingCost(request.getShippingCost());
        shipment.setEstimatedDelivery(request.getEstimatedDelivery());
        shipment.setActualDelivery(request.getActualDelivery());
        shipment.setStatus(request.getStatus());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 4. Update related order status
        Order order = shipment.getOrder();
        OrderStatus newOrderStatus = mapShipmentStatusToOrderStatus(request.getStatus());
        if (newOrderStatus != null) {
            orderStatusService.changeStatus(order, newOrderStatus, currentUser, null);

        }
        orderRepository.save(order);
        OrderShipment updatedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toResponse(updatedShipment);

    }

    private OrderStatus mapShipmentStatusToOrderStatus(ShipmentStatus shipmentStatus) {

        return switch (shipmentStatus) {

            case PROCESSING ->
                OrderStatus.PROCESSING;

            case SHIPPED, IN_TRANSIT ->
                OrderStatus.SHIPPED;

            case DELIVERED ->
                OrderStatus.DELIVERED;

            case CANCELLED ->
                OrderStatus.CANCELLED;

            default ->
                null;
        };
    }

}
