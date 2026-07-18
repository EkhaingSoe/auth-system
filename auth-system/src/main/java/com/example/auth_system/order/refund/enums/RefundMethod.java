package com.example.auth_system.order.refund.enums;

public enum RefundMethod {

    ORIGINAL_PAYMENT("Original Payment"),
    STORE_CREDIT("Store Credit"),
    BANK_TRANSFER("Bank Transfer"),
    CASH("Cash");

    private final String displayName;

    RefundMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
