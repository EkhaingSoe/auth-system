package com.example.auth_system.inventory.enums;

public enum InventoryCountStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    VERIFIED("Verified");

    private final String displayName;

    InventoryCountStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}