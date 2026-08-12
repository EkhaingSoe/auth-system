package com.example.auth_system.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String field;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.field = null;
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.field = null;
    }

    public BusinessException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.field = null;
    }

    public BusinessException(
            String field,
            String message,
            String errorCode,
            HttpStatus httpStatus) {

        super(message);
        this.field = field;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.field = null;
    }

    // ============================================================
    // DUPLICATE RESOURCE
    // ============================================================

    public static BusinessException duplicateResource(
            String resource,
            String field,
            String value) {

        return new BusinessException(
                field,
                resource + " already exists",
                "DUPLICATE_RESOURCE",
                HttpStatus.CONFLICT);
    }

    public static BusinessException duplicateUserEmail(String email) {
        return duplicateResource(
                "User with email " + email,
                "email",
                email);
    }

    public static BusinessException duplicateUsername(String username) {
        return duplicateResource(
                "Username " + username,
                "username",
                username);
    }

    public static BusinessException duplicateCustomerPhone(String phone) {
        return new BusinessException(
                "phone",
                "Phone number already exists",
                "DUPLICATE_CUSTOMER_PHONE",
                HttpStatus.CONFLICT);
    }

    public static BusinessException duplicateCustomerEmail(String email) {
        return new BusinessException(
                "email",
                "Email already exists",
                "DUPLICATE_CUSTOMER_EMAIL",
                HttpStatus.CONFLICT);
    }

    // ============================================================
    // PRODUCT
    // ============================================================

    public static BusinessException duplicateSku(String sku) {
        return new BusinessException(
                "SKU already exists: " + sku,
                "DUPLICATE_SKU",
                HttpStatus.CONFLICT);
    }

    public static BusinessException duplicateProductName(String name) {
        return new BusinessException(
                "Product name already exists: " + name,
                "DUPLICATE_PRODUCT_NAME",
                HttpStatus.CONFLICT);
    }

    public static BusinessException duplicateBarcode(String barcode) {
        return new BusinessException(
                "Barcode already exists: " + barcode,
                "DUPLICATE_BARCODE",
                HttpStatus.CONFLICT);
    }

    public static BusinessException invalidProductState(String message) {
        return new BusinessException(
                message,
                "INVALID_PRODUCT_STATE",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException productInactive(String productCode) {
        return new BusinessException(
                "Product is inactive: " + productCode,
                "PRODUCT_INACTIVE",
                HttpStatus.BAD_REQUEST);
    }

    // ============================================================
    // INVENTORY / STOCK
    // ============================================================

    public static BusinessException insufficientStock(
            String sku,
            int requested,
            int available) {

        return new BusinessException(
                "Insufficient stock for SKU " + sku
                        + ". Requested: " + requested
                        + ", Available: " + available,
                "INSUFFICIENT_STOCK",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException negativeStock(String sku) {
        return new BusinessException(
                "Stock quantity cannot be negative for SKU: " + sku,
                "NEGATIVE_STOCK",
                HttpStatus.BAD_REQUEST);
    }

    // ============================================================
    // SUPPLIER
    // ============================================================

    public static BusinessException supplierNotFound(String supplierId) {
        return new BusinessException(
                "Supplier not found with ID: " + supplierId,
                "SUPPLIER_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }

    public static BusinessException duplicateSupplierName(String name) {
        return new BusinessException(
                "name",
                "Supplier with name already exists: " + name,
                "DUPLICATE_SUPPLIER_NAME",
                HttpStatus.CONFLICT);
    }

    public static BusinessException duplicateSupplierPhone(String phone) {
        return new BusinessException(
                "phone",
                "Supplier with phone already exists: " + phone,
                "DUPLICATE_SUPPLIER_PHONE",
                HttpStatus.CONFLICT);
    }

    public static BusinessException duplicateSupplierEmail(String email) {
        return new BusinessException(
                "email",
                "Supplier with email already exists: " + email,
                "DUPLICATE_SUPPLIER_EMAIL",
                HttpStatus.CONFLICT);
    }

    // ============================================================
    // VARIANT
    // ============================================================

    public static BusinessException variantNotFound(String sku) {
        return new BusinessException(
                "Variant not found with SKU: " + sku,
                "VARIANT_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }

    // ============================================================
    // VALIDATION
    // ============================================================

    public static BusinessException validationError(String message) {
        return new BusinessException(
                message,
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException missingRequiredField(
            String fieldName) {

        return new BusinessException(
                fieldName,
                "Required field is missing",
                "MISSING_REQUIRED_FIELD",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException invalidPrice(String message) {
        return new BusinessException(
                "Invalid price: " + message,
                "INVALID_PRICE",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException invalidAttributeCombination(String message) {
        return new BusinessException(
                message,
                "INVALID_ATTRIBUTE_COMBINATION",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException invalidStoreHierarchy(String message) {
        return new BusinessException(
                message,
                "INVALID_STORE_HIERARCHY",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException storeHasChildStores() {
        return new BusinessException(
                "Cannot delete store with child stores. Delete child stores first.",
                "STORE_HAS_CHILD_STORES",
                HttpStatus.CONFLICT);
    }

    // brand
    public static BusinessException duplicateBrandName(String name) {
        return new BusinessException(
                "name",
                "Brand with name already exists: " + name,
                "DUPLICATE_BRAND_NAME",
                HttpStatus.CONFLICT);
    }

}