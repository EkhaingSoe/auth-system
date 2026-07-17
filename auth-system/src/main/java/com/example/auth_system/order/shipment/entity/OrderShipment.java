package com.example.auth_system.order.shipment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.auth_system.order.order.entity.Order;
import com.example.auth_system.order.shipment.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_shipments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDate actualDelivery;

    @Column(name = "shipping_cost")
    private BigDecimal shippingCost;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "carrier_phone")
    private String carrierPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}