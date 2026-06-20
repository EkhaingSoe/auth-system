// src/main/java/com/example/auth_system/store/service/StoreService.java
package com.example.auth_system.store.service;

import com.example.auth_system.store.dto.request.CreateStoreRequest;
import com.example.auth_system.store.dto.request.UpdateStoreRequest;
import com.example.auth_system.store.dto.response.StoreResponse;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreResponse createStore(CreateStoreRequest request);

    List<StoreResponse> getAllStores();

    StoreResponse getStoreById(UUID id);

    StoreResponse getStoreByCode(String storeCode);

    StoreResponse updateStore(UUID id, UpdateStoreRequest request);

    void deleteStore(UUID id);

    List<StoreResponse> searchStores(String searchTerm);

    List<StoreResponse> getStoresByStatus(String status);

    List<StoreResponse> getStoresByType(String storeType);

    List<StoreResponse> getChildStores(UUID parentId);

    List<StoreResponse> getHeadOffices();

    StoreResponse updateStoreStatus(UUID id, String status);
}