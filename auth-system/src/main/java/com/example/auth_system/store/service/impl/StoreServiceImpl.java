// src/main/java/com/example/auth_system/store/service/impl/StoreServiceImpl.java
package com.example.auth_system.store.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.store.dto.request.CreateStoreRequest;
import com.example.auth_system.store.dto.request.UpdateStoreRequest;
import com.example.auth_system.store.dto.response.StoreResponse;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.enums.StoreStatus;
import com.example.auth_system.store.enums.StoreType;
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

    @Override
    public StoreResponse createStore(CreateStoreRequest request) {
        log.info("Creating store: {}", request.getName());

        if (storeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Store with name '" + request.getName() + "' already exists");
        }

        Store store = storeMapper.toEntity(request);

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
        return storeMapper.toResponse(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return storeMapper.toResponseList(stores);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getStoreById(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
        return storeMapper.toResponse(store);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getStoreByCode(String storeCode) {
        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with code: " + storeCode));
        return storeMapper.toResponse(store);
    }

    @Override
    public StoreResponse updateStore(UUID id, UpdateStoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        storeMapper.updateEntity(store, request);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toResponse(savedStore);
    }

    @Override
    @Transactional
    public void deleteStore(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        List<Store> childStores = storeRepository.findChildStores(id);
        if (!childStores.isEmpty()) {
            throw BusinessException.storeHasChildStores();
        }

        storeRepository.delete(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> searchStores(String searchTerm) {
        log.info("Searching stores: {}", searchTerm);
        return storeMapper.toResponseList(storeRepository.searchStores(searchTerm));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresByStatus(StoreStatus status) {
        log.info("Fetching stores by status: {}", status);
        return storeMapper.toResponseList(storeRepository.findByStatus(status));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresByType(StoreType storeType) {
        log.info("Fetching stores by type: {}", storeType);
        return storeMapper.toResponseList(storeRepository.findByStoreType(storeType));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getChildStores(UUID parentId) {
        log.info("Fetching child stores for parent: {}", parentId);
        return storeMapper.toResponseList(storeRepository.findChildStores(parentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getHeadOffices() {
        log.info("Fetching head offices");
        return storeMapper.toResponseList(storeRepository.findHeadOffices());
    }

    @Override
    @Transactional
    public StoreResponse updateStoreStatus(UUID id, StoreStatus status) {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        store.setStatus(status);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toResponse(savedStore);
    }
}