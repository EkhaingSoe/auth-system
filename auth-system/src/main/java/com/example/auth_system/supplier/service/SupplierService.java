package com.example.auth_system.supplier.service;

import com.example.auth_system.supplier.dto.request.CreateSupplierRequest;
import com.example.auth_system.supplier.dto.request.UpdateSupplierRequest;
import com.example.auth_system.supplier.dto.response.SupplierResponse;

import java.util.List;
import java.util.UUID;

public interface SupplierService {

    SupplierResponse createSupplier(CreateSupplierRequest request);

    SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request);

    SupplierResponse getSupplierById(UUID id);

    SupplierResponse getSupplierByCode(String supplierCode);

    List<SupplierResponse> getAllSuppliers();

    List<SupplierResponse> getActiveSuppliers();

    List<SupplierResponse> searchSuppliers(String term);

    void deleteSupplier(UUID id);

    void activateSupplier(UUID id);

    void deactivateSupplier(UUID id);

    long countActiveSuppliers();

    List<SupplierResponse> getSuppliersByProductId(UUID productId);

    SupplierResponse getPrimarySupplierByProductId(UUID productId);
}