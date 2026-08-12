// src/main/java/com/example/auth_system/brand/service/impl/BrandServiceImpl.java
package com.example.auth_system.brand.service.impl;

import com.example.auth_system.brand.dto.request.CreateBrandRequest;
import com.example.auth_system.brand.dto.request.UpdateBrandRequest;
import com.example.auth_system.brand.dto.response.BrandResponse;
import com.example.auth_system.brand.entity.Brand;
import com.example.auth_system.brand.mapper.BrandMapper;
import com.example.auth_system.brand.repository.BrandRepository;
import com.example.auth_system.brand.service.BrandService;
import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

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

        if (brandRepository.existsByName(request.getName())) {
            throw BusinessException.duplicateBrandName(request.getName());
        }
        Brand brand = brandMapper.toEntity(request);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    @Override
    public Page<BrandResponse> getAllBrands(Pageable pageable) {
        log.info("Getting all brands");
        return brandRepository.findAll(pageable)
                .map(brandMapper::toResponse);
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

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        if (request.getName() != null && !request.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw BusinessException.duplicateBrandName(request.getName());
            }
        }

        brandMapper.updateEntity(brand, request);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(UUID id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        if (brand.getPublicId() != null) {
            try {
                cloudinaryService.deleteImage(brand.getPublicId());
            } catch (IOException e) {
                log.warn("Failed to delete logo from Cloudinary: {}", e.getMessage());
            }
        }

        brandRepository.delete(brand);
    }

    @Override
    public Page<BrandResponse> searchBrands(String searchTerm, Pageable pageable) {
        log.info("Searching brands: {}", searchTerm);
        return brandRepository.searchBrands(searchTerm, pageable)
                .map(brandMapper::toResponse);
    }

    @Override
    public Page<BrandResponse> getActiveBrands(Pageable pageable) {
        log.info("Getting active brands");
        return brandRepository.findByIsActiveTrue(pageable)
                .map(brandMapper::toResponse);
        // return brandMapper.toResponseList(brandRepository.findByIsActiveTrue());
    }

    // ============================================================
    // IMAGE MANAGEMENT
    // ============================================================

    @Override
    public BrandResponse uploadBrandLogo(UUID brandId, MultipartFile file) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        if (file == null || file.isEmpty()) {
            throw new BusinessException(
                    "logo",
                    "Brand logo file is required",
                    "BRAND_LOGO_REQUIRED",
                    HttpStatus.BAD_REQUEST);
        }

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
            Brand savedBrand = brandRepository.save(brand);
            return brandMapper.toResponse(savedBrand);

        } catch (IOException e) {
            log.error("Failed to upload logo: {}", e.getMessage());
            throw new BusinessException(
                    "logo",
                    "Failed to upload brand logo",
                    "BRAND_LOGO_UPLOAD_FAILED",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BrandResponse removeBrandLogo(UUID brandId) {

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
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }
}