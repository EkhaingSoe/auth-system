// src/main/java/com/example/auth_system/store/mapper/StoreMapper.java
package com.example.auth_system.store.mapper;

import com.example.auth_system.store.dto.request.CreateStoreRequest;
import com.example.auth_system.store.dto.request.UpdateStoreRequest;
import com.example.auth_system.store.dto.response.StoreResponse;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreMapper {

    private final StoreRepository storeRepository;

    public Store toEntity(CreateStoreRequest request) {
        Store parentStore = request.getParentStoreId() != null
                ? storeRepository.findById(request.getParentStoreId()).orElse(null)
                : null;

        return Store.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .storeType(request.getStoreType() != null ? request.getStoreType() : "BRANCH")
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .parentStore(parentStore)
                .settings(request.getSettings())
                .contactPerson(request.getContactPerson())
                .taxNumber(request.getTaxNumber())
                .build();
    }

    public void updateEntity(Store store, UpdateStoreRequest request) {
        if (request.getName() != null)
            store.setName(request.getName());
        if (request.getAddress() != null)
            store.setAddress(request.getAddress());
        if (request.getPhone() != null)
            store.setPhone(request.getPhone());
        if (request.getEmail() != null)
            store.setEmail(request.getEmail());
        if (request.getStoreType() != null)
            store.setStoreType(request.getStoreType());
        if (request.getStatus() != null)
            store.setStatus(request.getStatus());
        if (request.getSettings() != null)
            store.setSettings(request.getSettings());
        if (request.getContactPerson() != null)
            store.setContactPerson(request.getContactPerson());
        if (request.getTaxNumber() != null)
            store.setTaxNumber(request.getTaxNumber());

        if (request.getParentStoreId() != null) {
            Store parentStore = storeRepository.findById(request.getParentStoreId()).orElse(null);
            store.setParentStore(parentStore);
        }
    }

    public StoreResponse toResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .storeCode(store.getStoreCode())
                .name(store.getName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .email(store.getEmail())
                .storeType(store.getStoreType())
                .status(store.getStatus())
                .parentStoreId(store.getParentStore() != null ? store.getParentStore().getId() : null)
                .parentStoreName(store.getParentStore() != null ? store.getParentStore().getName() : null)
                .settings(store.getSettings())
                .contactPerson(store.getContactPerson())
                .taxNumber(store.getTaxNumber())
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .build();
    }

    public List<StoreResponse> toResponseList(List<Store> stores) {
        return stores.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}