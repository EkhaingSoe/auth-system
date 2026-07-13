package com.example.auth_system.order.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.order.dto.request.CreateShipmentRequest;
import com.example.auth_system.order.dto.response.ShippingResponse.ShipmentResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.OrderShipment;
import com.example.auth_system.order.mapper.ShipmentMapper;
import com.example.auth_system.order.repository.OrderRepository;
import com.example.auth_system.order.repository.OrderShipmentRepository;
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

    @Override
    public ShipmentResponse createShipment(UUID orderId, CreateShipmentRequest request) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found"));

        OrderShipment shipment = shipmentMapper.toEntity(request, order);

        OrderShipment savedShipment = shipmentRepository.save(shipment);

        return shipmentMapper.toResponse(savedShipment);

    }

}
