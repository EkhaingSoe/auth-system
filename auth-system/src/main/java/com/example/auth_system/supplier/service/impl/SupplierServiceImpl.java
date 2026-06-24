package com.example.auth_system.supplier.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.supplier.dto.request.CreateSupplierRequest;
import com.example.auth_system.supplier.dto.request.UpdateSupplierRequest;
import com.example.auth_system.supplier.dto.response.SupplierResponse;
import com.example.auth_system.supplier.entity.Supplier;
import com.example.auth_system.supplier.mapper.SupplierMapper;
import com.example.auth_system.supplier.repository.SupplierRepository;
import com.example.auth_system.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponse createSupplier(CreateSupplierRequest request) {
        log.info("Creating supplier: {}", request.getName());

        // Check duplicate name
        if (supplierRepository.existsByName(request.getName())) {
            throw new BusinessException("Supplier with name already exists: " + request.getName());
        }

        Supplier supplier = supplierMapper.toEntity(request);
        supplier = supplierRepository.save(supplier);

        log.info("Supplier created successfully with code: {}", supplier.getSupplierCode());
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request) {
        log.info("Updating supplier: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        // Check duplicate name if changed
        if (request.getName() != null && !supplier.getName().equals(request.getName())
                && supplierRepository.existsByName(request.getName())) {
            throw new BusinessException("Supplier with name already exists: " + request.getName());
        }

        supplierMapper.updateEntity(supplier, request);
        supplier = supplierRepository.save(supplier);

        log.info("Supplier updated successfully: {}", id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(UUID id) {
        log.info("Getting supplier by id: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierByCode(String supplierCode) {
        log.info("Getting supplier by code: {}", supplierCode);
        Supplier supplier = supplierRepository.findBySupplierCode(supplierCode)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + supplierCode));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        log.info("Getting all suppliers");
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getActiveSuppliers() {
        log.info("Getting active suppliers");
        return supplierRepository.findByIsActiveTrue().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> searchSuppliers(String term) {
        log.info("Searching suppliers with term: {}", term);
        return supplierRepository.searchSuppliers(term).stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSupplier(UUID id) {
        log.info("Deleting supplier: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        // Check if supplier has products
        // You can add product count check here

        supplier.setIsActive(false);
        supplierRepository.save(supplier);
        log.info("Supplier deactivated successfully: {}", id);
    }

    @Override
    public void activateSupplier(UUID id) {
        log.info("Activating supplier: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        supplier.setIsActive(true);
        supplierRepository.save(supplier);
        log.info("Supplier activated successfully: {}", id);
    }

    @Override
    public void deactivateSupplier(UUID id) {
        log.info("Deactivating supplier: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        supplier.setIsActive(false);
        supplierRepository.save(supplier);
        log.info("Supplier deactivated successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveSuppliers() {
        return supplierRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getSuppliersByProductId(UUID productId) {
        log.info("Getting suppliers for product: {}", productId);
        return supplierRepository.findSuppliersByProductId(productId).stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getPrimarySupplierByProductId(UUID productId) {
        log.info("Getting primary supplier for product: {}", productId);
        Supplier supplier = supplierRepository.findPrimarySupplierByProductId(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No primary supplier found for product: " + productId));
        return supplierMapper.toResponse(supplier);
    }
}