package com.example.auth_system.inventory.enums;

public enum AdjustmentType {

    ADDITION("Increase Stock"),

    REDUCTION("Decrease Stock"),

    DAMAGE("Damage/Wastage"),

    EXPIRY("Expired Stock"),

    THEFT("Theft/Loss"),

    REPLACEMENT("Replace Stock"),

    COUNT_ADJUSTMENT("Inventory Count Difference");

    private final String displayName;

    AdjustmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}