package com.example.auth_system.order.enums;

public enum DiscountType {
    PERCENTAGE("Percentage"),
    FIXED("Fixed Amount"),
    COUPON("Coupon");

    private final String displayName;

    DiscountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}