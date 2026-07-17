package com.example.auth_system.order.controller;

import com.example.auth_system.order.enums.OrderStatus;
import com.example.auth_system.order.order.dto.orderRequest.CreateOrderRequest;
import com.example.auth_system.order.order.dto.orderResponse.OrderResponse;
import com.example.auth_system.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

        private final OrderService orderService;

        @PostMapping
        public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {

                OrderResponse response = orderService.createOrder(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/{orderId}")
        public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID orderId) {

                return ResponseEntity.ok(
                                orderService.getOrderById(orderId));
        }

        @GetMapping("/number/{orderNumber}")
        public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {

                return ResponseEntity.ok(
                                orderService.getOrderByNumber(orderNumber));
        }

        @GetMapping
        public ResponseEntity<List<OrderResponse>> getAllOrders() {

                return ResponseEntity.ok(
                                orderService.getAllOrders());
        }

        @GetMapping("/customer/{customerId}")
        public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable UUID customerId) {

                return ResponseEntity.ok(
                                orderService.getOrdersByCustomer(customerId));
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {

                return ResponseEntity.ok(
                                orderService.getOrdersByStatus(status));
        }

        @GetMapping("/search")
        public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam String keyword) {

                return ResponseEntity.ok(
                                orderService.searchOrders(keyword));
        }

        @GetMapping("/date-range")
        public ResponseEntity<List<OrderResponse>> getOrdersByDateRange(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,

                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

                return ResponseEntity.ok(
                                orderService.getOrdersByDateRange(start, end));
        }

        @PatchMapping("/{orderId}/status")
        public ResponseEntity<OrderResponse> updateOrderStatus(
                        @PathVariable UUID orderId,
                        @RequestParam OrderStatus status,
                        @RequestParam(required = false) String reason) {

                return ResponseEntity.ok(
                                orderService.updateOrderStatus(
                                                orderId,
                                                status,
                                                reason));
        }

        @PatchMapping("/{orderId}/cancel")
        public ResponseEntity<OrderResponse> cancelOrder(
                        @PathVariable UUID orderId,
                        @RequestParam String reason) {

                return ResponseEntity.ok(
                                orderService.cancelOrder(
                                                orderId,
                                                reason));
        }

        @GetMapping("/count")
        public ResponseEntity<Long> countOrdersByStatus(
                        @RequestParam OrderStatus status) {

                return ResponseEntity.ok(
                                orderService.countOrdersByStatus(status));
        }

        @GetMapping("/revenue")
        public ResponseEntity<BigDecimal> getTotalRevenue() {

                return ResponseEntity.ok(
                                orderService.getTotalRevenue());
        }

        @GetMapping("/revenue/date-range")
        public ResponseEntity<BigDecimal> getRevenueBetween(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,

                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

                return ResponseEntity.ok(
                                orderService.getRevenueBetween(start, end));
        }

        @DeleteMapping("/{orderId}")
        public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {

                orderService.deleteOrder(orderId);

                return ResponseEntity.noContent().build();
        }
}