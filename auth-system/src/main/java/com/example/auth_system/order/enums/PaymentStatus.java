package com.example.auth_system.order.enums;

public enum PaymentStatus {
    UNPAID("Unpaid"),
    PAID("Paid"),
    PARTIALLY_PAID("Partially Paid"),
    REFUNDED("Refunded"),
    PARTIALLY_REFUNDED("Partially Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}