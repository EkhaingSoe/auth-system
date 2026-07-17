package com.example.auth_system.order.order.enums;

public enum FulfillmentStatus {
    UNFULFILLED("Unfulfilled"),
    FULFILLED("Fulfilled");

    private final String displayName;

    FulfillmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}