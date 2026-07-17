package com.example.auth_system.order.payment.enums;

public enum PaymentMethod {

    CASH("Cash"),
    CARD("Card"),
    KBZ_PAY("KPay"),
    BANK_TRANSFER("Bank Transfer"),

    KBZ_QR("KBZ QR"),
    AYA_QR("AYA QR");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}