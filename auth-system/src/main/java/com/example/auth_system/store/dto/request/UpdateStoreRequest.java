// src/main/java/com/example/auth_system/store/dto/request/UpdateStoreRequest.java
package com.example.auth_system.store.dto.request;

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
public class UpdateStoreRequest {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String storeType;
    private String status;
    private UUID parentStoreId;
    private JsonNode settings;
    private String contactPerson;
    private String taxNumber;
}
