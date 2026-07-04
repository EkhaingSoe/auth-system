package com.example.auth_system.order.enums;

public enum FulfillmentStatus {
    UNFULFILLED("Unfulfilled"),
    PARTIALLY_FULFILLED("Partially Fulfilled"),
    FULFILLED("Fulfilled");

    private final String displayName;

    FulfillmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}