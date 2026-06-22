// src/main/java/com/example/auth_system/brand/service/BrandService.java
package com.example.auth_system.brand.service;

import com.example.auth_system.brand.dto.request.CreateBrandRequest;
import com.example.auth_system.brand.dto.request.UpdateBrandRequest;
import com.example.auth_system.brand.dto.response.BrandResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    BrandResponse createBrand(CreateBrandRequest request);

    List<BrandResponse> getAllBrands();

    BrandResponse getBrandById(UUID id);

    BrandResponse getBrandByName(String name);

    BrandResponse updateBrand(UUID id, UpdateBrandRequest request);

    void deleteBrand(UUID id);

    List<BrandResponse> searchBrands(String searchTerm);

    List<BrandResponse> getActiveBrands();

    // Image Management
    BrandResponse uploadBrandLogo(UUID brandId, MultipartFile file);

    BrandResponse removeBrandLogo(UUID brandId);
}