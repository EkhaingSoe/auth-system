// src/main/java/com/example/auth_system/brand/service/impl/BrandServiceImpl.java
package com.example.auth_system.brand.service.impl;

import com.example.auth_system.brand.dto.request.CreateBrandRequest;
import com.example.auth_system.brand.dto.request.UpdateBrandRequest;
import com.example.auth_system.brand.dto.response.BrandResponse;
import com.example.auth_system.brand.entity.Brand;
import com.example.auth_system.brand.mapper.BrandMapper;
import com.example.auth_system.brand.repository.BrandRepository;
import com.example.auth_system.brand.service.BrandService;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public BrandResponse createBrand(CreateBrandRequest request) {
        log.info("Creating brand: {}", request.getName());

        if (brandRepository.existsByName(request.getName())) {
            throw new RuntimeException("Brand with name '" + request.getName() + "' already exists");
        }

        Brand brand = brandMapper.toEntity(request);
        brand = brandRepository.save(brand);

        log.info("Brand created: {}", brand.getId());
        return brandMapper.toResponse(brand);
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        log.info("Getting all brands");
        return brandMapper.toResponseList(brandRepository.findAll());
    }

    @Override
    public BrandResponse getBrandById(UUID id) {
        log.info("Getting brand by id: {}", id);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return brandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse getBrandByName(String name) {
        log.info("Getting brand by name: {}", name);
        Brand brand = brandRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with name: " + name));
        return brandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse updateBrand(UUID id, UpdateBrandRequest request) {
        log.info("Updating brand: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        if (request.getName() != null && !request.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw new RuntimeException("Brand with name '" + request.getName() + "' already exists");
            }
        }

        brandMapper.updateEntity(brand, request);
        brand = brandRepository.save(brand);

        log.info("Brand updated: {}", id);
        return brandMapper.toResponse(brand);
    }

    @Override
    public void deleteBrand(UUID id) {
        log.info("Deleting brand: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        // Delete logo from Cloudinary if exists
        if (brand.getPublicId() != null) {
            try {
                cloudinaryService.deleteImage(brand.getPublicId());
            } catch (IOException e) {
                log.warn("Failed to delete logo from Cloudinary: {}", e.getMessage());
            }
        }

        brandRepository.delete(brand);
        log.info("Brand deleted: {}", id);
    }

    @Override
    public List<BrandResponse> searchBrands(String searchTerm) {
        log.info("Searching brands: {}", searchTerm);
        return brandMapper.toResponseList(brandRepository.searchBrands(searchTerm));
    }

    @Override
    public List<BrandResponse> getActiveBrands() {
        log.info("Getting active brands");
        return brandMapper.toResponseList(brandRepository.findByIsActiveTrue());
    }

    // ============================================================
    // IMAGE MANAGEMENT
    // ============================================================

    @Override
    public BrandResponse uploadBrandLogo(UUID brandId, MultipartFile file) {
        log.info("Uploading logo for brand: {}", brandId);

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        try {
            // Delete old logo if exists
            if (brand.getPublicId() != null) {
                cloudinaryService.deleteImage(brand.getPublicId());
            }

            // Upload new logo to Cloudinary
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(file, "brands");

            String imageUrl = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            brand.setLogoUrl(imageUrl);
            brand.setPublicId(publicId);
            brand = brandRepository.save(brand);

            log.info("Logo uploaded for brand: {}", brandId);
            return brandMapper.toResponse(brand);

        } catch (IOException e) {
            log.error("Failed to upload logo: {}", e.getMessage());
            throw new RuntimeException("Failed to upload logo: " + e.getMessage());
        }
    }

    @Override
    public BrandResponse removeBrandLogo(UUID brandId) {
        log.info("Removing logo for brand: {}", brandId);

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        if (brand.getPublicId() != null) {
            try {
                cloudinaryService.deleteImage(brand.getPublicId());
            } catch (IOException e) {
                log.warn("Failed to delete logo from Cloudinary: {}", e.getMessage());
            }
        }

        brand.setLogoUrl(null);
        brand.setPublicId(null);
        brand = brandRepository.save(brand);

        log.info("Logo removed for brand: {}", brandId);
        return brandMapper.toResponse(brand);
    }
}