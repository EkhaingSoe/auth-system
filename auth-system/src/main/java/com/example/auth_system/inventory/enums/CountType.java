package com.example.auth_system.inventory.enums;

public enum CountType {
    FULL_COUNT("Full Inventory Count"),

    CYCLE_COUNT("Cycle Inventory Count"),

    SPOT_CHECK("Spot Check");

    private final String displayName;

    CountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
