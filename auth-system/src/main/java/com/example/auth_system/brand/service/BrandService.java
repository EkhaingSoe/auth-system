// src/main/java/com/example/auth_system/brand/service/BrandService.java
package com.example.auth_system.brand.service;

import com.example.auth_system.brand.dto.request.CreateBrandRequest;
import com.example.auth_system.brand.dto.request.UpdateBrandRequest;
import com.example.auth_system.brand.dto.response.BrandResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    BrandResponse createBrand(CreateBrandRequest request);

    Page<BrandResponse> getAllBrands(Pageable pageable);

    BrandResponse getBrandById(UUID id);

    BrandResponse getBrandByName(String name);

    BrandResponse updateBrand(UUID id, UpdateBrandRequest request);

    void deleteBrand(UUID id);

    Page<BrandResponse> searchBrands(String searchTerm, Pageable pageable);

    Page<BrandResponse> getActiveBrands(Pageable pageable);

    // Image Management
    BrandResponse uploadBrandLogo(UUID brandId, MultipartFile file);

    BrandResponse removeBrandLogo(UUID brandId);
}