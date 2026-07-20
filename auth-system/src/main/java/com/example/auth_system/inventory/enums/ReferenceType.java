package com.example.auth_system.inventory.enums;

public enum ReferenceType {

    ORDER("Customer Order"),

    PURCHASE_ORDER("Purchase Order"),

    SALES_RETURN("Customer Return"),

    SUPPLIER_RETURN("Supplier Return"),

    STOCK_ADJUSTMENT("Manual Stock Adjustment"),

    INVENTORY_COUNT("Inventory Count"),

    STOCK_TRANSFER("Warehouse Transfer"),

    INITIAL_STOCK("Initial Stock"),

    MANUAL("Manual Entry");

    private final String displayName;

    ReferenceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}