// src/main/java/com/example/auth_system/store/dto/request/CreateStoreRequest.java
package com.example.auth_system.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequest {

    @NotBlank(message = "Store name is required")
    private String name;

    private String address;
    private String phone;
    private String email;

    private String storeType; // HEAD_OFFICE, BRANCH, WAREHOUSE
    private String status; // ACTIVE, INACTIVE, SUSPENDED, CLOSED

    private UUID parentStoreId;
    private JsonNode settings;
    private String contactPerson;
    private String taxNumber;
}