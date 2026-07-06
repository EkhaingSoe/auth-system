package com.example.auth_system.order.enums;

public enum RefundType {
    FULL("Full"),
    PARTIAL("Partial");

    private final String displayName;

    RefundType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
