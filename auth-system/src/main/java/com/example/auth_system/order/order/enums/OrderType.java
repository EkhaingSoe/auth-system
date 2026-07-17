package com.example.auth_system.order.order.enums;

public enum OrderType {
    POS("Point of Sale"),
    ONLINE("Online"),
    WHOLESALE("Wholesale");

    private final String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}