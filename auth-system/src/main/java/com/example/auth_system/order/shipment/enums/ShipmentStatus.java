package com.example.auth_system.order.shipment.enums;

public enum ShipmentStatus {

    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    IN_TRANSIT("In Transit"),
    DELIVERED("Delivered"),
    FAILED("Failed"),
    RETURNED("Returned"),
    CANCELLED("Cancelled");

    private final String displayName;

    ShipmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}