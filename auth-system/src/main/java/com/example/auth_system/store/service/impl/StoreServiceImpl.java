// src/main/java/com/example/auth_system/store/service/impl/StoreServiceImpl.java
package com.example.auth_system.store.service.impl;

import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.store.dto.request.CreateStoreRequest;
import com.example.auth_system.store.dto.request.UpdateStoreRequest;
import com.example.auth_system.store.dto.response.StoreResponse;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.mapper.StoreMapper;
import com.example.auth_system.store.repository.StoreRepository;
import com.example.auth_system.store.service.StoreService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final ObjectMapper objectMapper;

    // @Override
    // public StoreResponse createStore(CreateStoreRequest request) {
    // log.info("Creating store: {}", request.getName());

    // if (storeRepository.existsByStoreCode(request.getName())) {
    // throw new RuntimeException("Store with name '" + request.getName() + "'
    // already exists");
    // }

    // Store store = storeMapper.toEntity(request);
    // store = storeRepository.save(store);

    // log.info("Store created with code: {}", store.getStoreCode());
    // return storeMapper.toResponse(store);
    // }

    @Override
    public StoreResponse createStore(CreateStoreRequest request) {
        log.info("Creating store: {}", request.getName());

        if (storeRepository.existsByStoreCode(request.getName())) {
            throw new RuntimeException("Store with name '" + request.getName() + "' already exists");
        }

        Store store = storeMapper.toEntity(request);

        // ✅ Set default settings if null
        if (store.getSettings() == null) {
            try {
                JsonNode defaultSettings = objectMapper
                        .readTree("{\"currency\": \"MMK\", \"taxRate\": 5, \"timezone\": \"Asia/Yangon\"}");
                store.setSettings(defaultSettings);
            } catch (Exception e) {
                log.warn("Could not set default settings: {}", e.getMessage());
            }
        }

        store = storeRepository.save(store);

        log.info("Store created with code: {}", store.getStoreCode());
        return storeMapper.toResponse(store);
    }

    @Override
    public List<StoreResponse> getAllStores() {
        log.info("Fetching all stores");
        return storeMapper.toResponseList(storeRepository.findAll());
    }

    @Override
    public StoreResponse getStoreById(UUID id) {
        log.info("Fetching store by id: {}", id);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
        return storeMapper.toResponse(store);
    }

    @Override
    public StoreResponse getStoreByCode(String storeCode) {
        log.info("Fetching store by code: {}", storeCode);
        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with code: " + storeCode));
        return storeMapper.toResponse(store);
    }

    @Override
    public StoreResponse updateStore(UUID id, UpdateStoreRequest request) {
        log.info("Updating store: {}", id);

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        storeMapper.updateEntity(store, request);
        store = storeRepository.save(store);

        log.info("Store updated: {}", store.getStoreCode());
        return storeMapper.toResponse(store);
    }

    @Override
    public void deleteStore(UUID id) {
        log.info("Deleting store: {}", id);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        // Check if store has child stores
        List<Store> childStores = storeRepository.findChildStores(id);
        if (!childStores.isEmpty()) {
            throw new RuntimeException("Cannot delete store with child stores. Delete child stores first.");
        }

        storeRepository.delete(store);
        log.info("Store deleted: {}", id);
    }

    @Override
    public List<StoreResponse> searchStores(String searchTerm) {
        log.info("Searching stores: {}", searchTerm);
        return storeMapper.toResponseList(storeRepository.searchStores(searchTerm));
    }

    @Override
    public List<StoreResponse> getStoresByStatus(String status) {
        log.info("Fetching stores by status: {}", status);
        return storeMapper.toResponseList(storeRepository.findByStatus(status));
    }

    @Override
    public List<StoreResponse> getStoresByType(String storeType) {
        log.info("Fetching stores by type: {}", storeType);
        return storeMapper.toResponseList(storeRepository.findByStoreType(storeType));
    }

    @Override
    public List<StoreResponse> getChildStores(UUID parentId) {
        log.info("Fetching child stores for parent: {}", parentId);
        return storeMapper.toResponseList(storeRepository.findChildStores(parentId));
    }

    @Override
    public List<StoreResponse> getHeadOffices() {
        log.info("Fetching head offices");
        return storeMapper.toResponseList(storeRepository.findHeadOffices());
    }

    @Override
    public StoreResponse updateStoreStatus(UUID id, String status) {
        log.info("Updating store status: {} -> {}", id, status);

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        store.setStatus(status);
        store = storeRepository.save(store);

        log.info("Store status updated: {}", store.getStoreCode());
        return storeMapper.toResponse(store);
    }
}