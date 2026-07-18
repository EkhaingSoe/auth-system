package com.example.auth_system.order.refund.enums;

public enum RefundStatus {

    PENDING("Pending"),
    APPROVED("Approved"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    REJECTED("Rejected");

    private final String displayName;

    RefundStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
