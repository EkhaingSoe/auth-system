package com.example.auth_system.product.controller;

import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.product.dto.request.AddAttributeValueRequest;
import com.example.auth_system.product.dto.request.CreateAttributeRequest;
import com.example.auth_system.product.dto.request.UpdateAttributeRequest;
import com.example.auth_system.product.dto.response.ProductAttributeResponse;
import com.example.auth_system.product.entity.ProductAttribute;
import com.example.auth_system.product.entity.ProductAttributeValue;
import com.example.auth_system.product.service.ProductAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/product-attributes")
@RequiredArgsConstructor
@Slf4j
public class ProductAttributeController {

        private final ProductAttributeService attributeService;

        // ============================================================
        // GET ACTIVE ATTRIBUTES (Main API for UI)
        // ============================================================

        @GetMapping("/active")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<ProductAttributeResponse>>> getActiveAttributes() {
                log.info("GET /api/admin/product-attributes/active - Getting active attributes with values");
                List<ProductAttributeResponse> attributes = attributeService.getActiveAttributesWithValues();
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Active attributes retrieved successfully", attributes));
        }

        // ============================================================
        // CRUD OPERATIONS
        // ============================================================

        @GetMapping
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<List<ProductAttributeResponse>>> getAllAttributes() {
                log.info("GET /api/admin/product-attributes - Getting all attributes");
                List<ProductAttributeResponse> attributes = attributeService.getAllAttributes();
                return ResponseEntity.ok(
                                ApiResponse.success(200, "All attributes retrieved successfully", attributes));
        }

        @GetMapping("/{id}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_READ')")
        public ResponseEntity<ApiResponse<ProductAttributeResponse>> getAttributeById(@PathVariable UUID id) {
                log.info("GET /api/admin/product-attributes/{} - Getting attribute by id", id);
                ProductAttributeResponse attribute = attributeService.getAttributeById(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Attribute retrieved successfully", attribute));
        }

        @PostMapping
        @PreAuthorize("@permission.hasPermission('PRODUCT_CREATE')")
        public ResponseEntity<ApiResponse<ProductAttributeResponse>> createAttribute(
                        @Valid @RequestBody CreateAttributeRequest request) {
                log.info("POST /api/admin/product-attributes - Creating attribute: {}", request.getName());

                ProductAttribute attribute = ProductAttribute.builder()
                                .name(request.getName())
                                .displayName(request.getDisplayName() != null ? request.getDisplayName()
                                                : request.getName())
                                .attributeType(request.getAttributeType())
                                .isActive(true)
                                .build();

                ProductAttributeResponse response = attributeService.createAttribute(attribute);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(201, "Attribute created successfully", response));
        }

        @PutMapping("/{id}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
        public ResponseEntity<ApiResponse<ProductAttributeResponse>> updateAttribute(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateAttributeRequest request) {
                log.info("PUT /api/admin/product-attributes/{} - Updating attribute", id);

                ProductAttribute attribute = ProductAttribute.builder()
                                .name(request.getName())
                                .displayName(request.getDisplayName())
                                .attributeType(request.getAttributeType())
                                .isActive(request.getIsActive())
                                .build();

                ProductAttributeResponse response = attributeService.updateAttribute(id, attribute);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Attribute updated successfully", response));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_DELETE')")
        public ResponseEntity<ApiResponse<Void>> deleteAttribute(@PathVariable UUID id) {
                log.info("DELETE /api/admin/product-attributes/{} - Deleting attribute", id);
                attributeService.deleteAttribute(id);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Attribute deactivated successfully", null));
        }

        // ============================================================
        // ATTRIBUTE VALUE OPERATIONS
        // ============================================================

        @PostMapping("/{attributeId}/values")
        @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
        public ResponseEntity<ApiResponse<ProductAttributeResponse>> addValueToAttribute(
                        @PathVariable UUID attributeId,
                        @Valid @RequestBody AddAttributeValueRequest request) {
                log.info("POST /api/admin/product-attributes/{}/values - Adding value: {}",
                                attributeId, request.getValue());

                ProductAttributeValue value = ProductAttributeValue.builder()
                                .value(request.getValue())
                                .hexCode(request.getHexCode())
                                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                                .isActive(true)
                                .build();

                ProductAttributeResponse response = attributeService.addValueToAttribute(attributeId, value);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(201, "Value added successfully", response));
        }

        // @PutMapping("/{attributeId}/values/{valueId}")
        // @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
        // public ResponseEntity<ApiResponse<ProductAttributeResponse>>
        // updateAttributeValue(
        // @PathVariable UUID attributeId,
        // @PathVariable UUID valueId,
        // @Valid @RequestBody UpdateAttributeValueRequest request) {
        // log.info("PUT /api/admin/product-attributes/{}/values/{} - Updating value",
        // attributeId, valueId);

        // ProductAttributeValue value = ProductAttributeValue.builder()
        // .value(request.getValue())
        // .hexCode(request.getHexCode())
        // .displayOrder(request.getDisplayOrder())
        // .isActive(request.getIsActive())
        // .build();

        // ProductAttributeResponse response =
        // attributeService.updateAttribute(attributeId, valueId, value);
        // return ResponseEntity.ok(
        // ApiResponse.success(200, "Value updated successfully", response));
        // }

        @DeleteMapping("/{attributeId}/values/{valueId}")
        @PreAuthorize("@permission.hasPermission('PRODUCT_UPDATE')")
        public ResponseEntity<ApiResponse<Void>> removeValueFromAttribute(
                        @PathVariable UUID attributeId,
                        @PathVariable UUID valueId) {
                log.info("DELETE /api/admin/product-attributes/{}/values/{} - Removing value",
                                attributeId, valueId);
                attributeService.removeValueFromAttribute(attributeId, valueId);
                return ResponseEntity.ok(
                                ApiResponse.success(200, "Value deactivated successfully", null));
        }
}