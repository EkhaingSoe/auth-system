package com.example.auth_system.order.enums;

public enum PaymentMethod {
    CASH("Cash"),
    CARD("Card"),
    QR_CODE("QR Code"),
    BANK_TRANSFER("Bank Transfer"),
    KPAY("KPay");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}