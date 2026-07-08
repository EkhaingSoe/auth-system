package com.example.auth_system.order.service.impl;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.auth.repository.UserRepository;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.customer.repository.CustomerRepository;
import com.example.auth_system.order.dto.request.CreateOrderItemRequest;
import com.example.auth_system.order.dto.request.CreateOrderRequest;
import com.example.auth_system.order.dto.request.UpdateOrderRequest;
import com.example.auth_system.order.dto.response.orderResponse.OrderResponse;
import com.example.auth_system.order.entity.Order;
import com.example.auth_system.order.entity.OrderItem;
import com.example.auth_system.order.entity.OrderStatusHistory;
import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.enums.PaymentStatus;
import com.example.auth_system.order.mapper.OrderItemMapper;
import com.example.auth_system.order.mapper.OrderMapper;
import com.example.auth_system.order.repository.OrderItemRepository;
import com.example.auth_system.order.repository.OrderRepository;
import com.example.auth_system.order.repository.OrderStatusHistoryRepository;
import com.example.auth_system.order.service.OrderService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.product.repository.ProductRepository;
import com.example.auth_system.product.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    // @Override
    // public OrderResponse createOrder(CreateOrderRequest request) {
    // log.info("Creating order...");

    // Customer customer = null;
    // if (request.getCustomerId() != null) {
    // customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
    // () -> new RuntimeException("Customer not found"));
    // }

    // List<OrderItem> orderItems = new ArrayList<>();
    // BigDecimal subtotal = BigDecimal.ZERO;
    // for (CreateOrderItemRequest itemRequest : request.getItems()) {
    // Product product =
    // productRepository.findById(itemRequest.getProductId()).orElseThrow(
    // () -> new RuntimeException("Product not found"));

    // ProductVariant variant = null;

    // if (itemRequest.getVariantId() != null) {
    // variant = variantRepository.findById(itemRequest.getVariantId()).orElseThrow(
    // () -> new RuntimeException("Variant not found"));

    // }
    // }

    // }

}