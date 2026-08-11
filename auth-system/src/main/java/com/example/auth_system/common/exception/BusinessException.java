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

    public static BusinessException duplicateSku(String sku) {
        return new BusinessException(
                "SKU already exists: " + sku,
                "DUPLICATE_SKU",
                HttpStatus.CONFLICT);
    }

    public static BusinessException insufficientStock(String sku, int requested, int available) {
        return new BusinessException(
                "Insufficient stock for SKU " + sku + ". Requested: " + requested + ", Available: " + available,
                "INSUFFICIENT_STOCK",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException invalidProductState(String message) {
        return new BusinessException(
                message,
                "INVALID_PRODUCT_STATE",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException duplicateProductName(String name) {
        return new BusinessException(
                "Product name already exists: " + name,
                "DUPLICATE_PRODUCT_NAME",
                HttpStatus.CONFLICT);
    }

    public static BusinessException invalidAttributeCombination(String message) {
        return new BusinessException(
                message,
                "INVALID_ATTRIBUTE_COMBINATION",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException supplierNotFound(String supplierId) {
        return new BusinessException(
                "Supplier not found with ID: " + supplierId,
                "SUPPLIER_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }

    public static BusinessException validationError(String message) {
        return new BusinessException(
                message,
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException productInactive(String productCode) {
        return new BusinessException(
                "Product is inactive: " + productCode,
                "PRODUCT_INACTIVE",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException variantNotFound(String sku) {
        return new BusinessException(
                "Variant not found with SKU: " + sku,
                "VARIANT_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }

    public static BusinessException duplicateBarcode(String barcode) {
        return new BusinessException(
                "Barcode already exists: " + barcode,
                "DUPLICATE_BARCODE",
                HttpStatus.CONFLICT);
    }

    public static BusinessException invalidPrice(String message) {
        return new BusinessException(
                "Invalid price: " + message,
                "INVALID_PRICE",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException negativeStock(String sku) {
        return new BusinessException(
                "Stock quantity cannot be negative for SKU: " + sku,
                "NEGATIVE_STOCK",
                HttpStatus.BAD_REQUEST);
    }

    public static BusinessException missingRequiredField(String fieldName) {
        return new BusinessException(
                "Required field is missing: " + fieldName,
                "MISSING_REQUIRED_FIELD",
                HttpStatus.BAD_REQUEST);
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
}