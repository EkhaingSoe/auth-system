package com.example.auth_system.inventory.enums;

public enum AdjustmentType {
    ADDITION("Increase Stock"),
    REDUCTION("Decrease Stock"),
    REPLACEMENT("Replace Stock"),
    DAMAGE("Damage/Wastage"),
    EXPIRY("Expired Stock"),
    THEFT("Theft/Loss");

    private final String displayName;

    AdjustmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}