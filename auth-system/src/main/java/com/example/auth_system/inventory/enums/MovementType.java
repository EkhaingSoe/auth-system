package com.example.auth_system.inventory.enums;

public enum MovementType {
    PURCHASE_IN("Purchase"),
    SALES_OUT("Sales"),
    RETURN_IN("Return from Customer"),
    RETURN_OUT("Return to Supplier"),
    TRANSFER_IN("Transfer In"),
    TRANSFER_OUT("Transfer Out"),
    ADJUSTMENT("Manual Adjustment"),
    WASTAGE("Wastage/Damage"),
    COUNT_ADJUST("Inventory Count Adjustment");

    private final String displayName;

    MovementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}