package com.example.auth_system.store.dto.response;

import com.example.auth_system.store.enums.StoreStatus;
import com.example.auth_system.store.enums.StoreType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {

    private UUID id;

    private String storeCode;

    private String name;

    private String address;

    private String phone;

    private String email;

    private StoreType storeType;

    private StoreStatus status;

    private UUID parentStoreId;

    private String parentStoreName;

    private JsonNode settings;

    private String contactPerson;

    private String taxNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
