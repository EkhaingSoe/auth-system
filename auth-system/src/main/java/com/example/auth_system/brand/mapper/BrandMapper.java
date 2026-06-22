// src/main/java/com/example/auth_system/brand/mapper/BrandMapper.java
package com.example.auth_system.brand.mapper;

import com.example.auth_system.brand.dto.request.CreateBrandRequest;
import com.example.auth_system.brand.dto.request.UpdateBrandRequest;
import com.example.auth_system.brand.dto.response.BrandResponse;
import com.example.auth_system.brand.entity.Brand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {

    public Brand toEntity(CreateBrandRequest request) {
        return Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    public void updateEntity(Brand brand, UpdateBrandRequest request) {
        if (request.getName() != null)
            brand.setName(request.getName());
        if (request.getDescription() != null)
            brand.setDescription(request.getDescription());
        if (request.getIsActive() != null)
            brand.setIsActive(request.getIsActive());
    }

    public BrandResponse toResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .isActive(brand.getIsActive())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public List<BrandResponse> toResponseList(List<Brand> brands) {
        return brands.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}