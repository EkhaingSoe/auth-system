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
import com.example.auth_system.order.enums.FulfillmentStatus;
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order...");

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                    () -> new ResourceNotFoundException("Customer not found"));
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CreateOrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow(
                    () -> new ResourceNotFoundException("Product not found"));

            ProductVariant variant = null;

            if (itemRequest.getVariantId() != null) {
                variant = variantRepository.findById(itemRequest.getVariantId()).orElseThrow(
                        () -> new ResourceNotFoundException("Variant not found"));

            }
            OrderItem orderItem = orderItemMapper.toEntity(itemRequest, product, variant);
            BigDecimal price = variant != null ? variant.getSellingPrice() : product.getMinPrice();
            orderItem.setUnitPrice(price);
            BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setTotalPrice(totalPrice);
            orderItem.setProductName(product.getName());
            orderItem.setProductSku(product.getProductCode());
            orderItem.setVariantSku(variant != null ? variant.getSku() : null);
            orderItem.setVariantAttributes(variant != null ? variant.getAttributeValues() : null);
            subtotal = subtotal.add(totalPrice);
            orderItems.add(orderItem);
        }

        Order order = orderMapper.toEntity(request, customer, orderItems, subtotal);
        order.setOrderNumber(generateOrderNumber());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        order.setCreatedBy(currentUser);
        // Initial status
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setFulfillmentStatus(FulfillmentStatus.UNFULFILLED);

        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setShippingCost(BigDecimal.ZERO);
        BigDecimal grandTotal = subtotal
                .subtract(order.getDiscountAmount())
                .add(order.getTaxAmount())
                .add(order.getShippingCost());

        order.setGrandTotal(grandTotal);
        Order savedOrder = orderRepository.save(order);
        addOrderStatusHistory(
                savedOrder,
                null,
                OrderStatus.PENDING,
                currentUser,
                "Order created");
        return orderMapper.toResponse(savedOrder);

    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(String orderNumber) {

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(UUID customerId) {

        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = orderRepository.findByOrderStatus(status);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {

        List<Order> orders = orderRepository.findByOrderDateBetween(start, end);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> searchOrders(String searchTerm) {

        List<Order> orders = orderRepository.searchOrders(searchTerm);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus, String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus previousStatus = order.getOrderStatus();

        order.setOrderStatus(newStatus);

        switch (newStatus) {
            case PROCESSING -> order.setProcessingDate(LocalDateTime.now());
            case SHIPPED -> order.setShippedDate(LocalDateTime.now());
            case DELIVERED -> order.setDeliveredDate(LocalDateTime.now());
            case CANCELLED -> order.setCancelledDate(LocalDateTime.now());
            default -> {
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(currentUser)
                .reason(reason)
                .build();

        statusHistoryRepository.save(history);

        orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    private void addOrderStatusHistory(
            Order order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            User user,
            String reason) {

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(user)
                .reason(reason)
                .build();

        statusHistoryRepository.save(history);
    }

}