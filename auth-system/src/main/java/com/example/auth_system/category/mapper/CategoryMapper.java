package com.example.auth_system.category.mapper;

import com.example.auth_system.category.dto.request.CreateCategoryRequest;
import com.example.auth_system.category.dto.request.UpdateCategoryRequest;
import com.example.auth_system.category.dto.response.CategoryResponse;
import com.example.auth_system.category.entity.Category;
import com.example.auth_system.category.entity.CategoryImage;
import com.example.auth_system.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final CategoryRepository categoryRepository;

    public Category toEntity(CreateCategoryRequest request) {
        Category parentCategory = request.getParentCategoryId() != null
                ? categoryRepository.findById(request.getParentCategoryId()).orElse(null)
                : null;

        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(request.getSlug())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .parentCategory(parentCategory)
                .build();
    }

    public void updateEntity(Category category, UpdateCategoryRequest request) {
        if (request.getName() != null)
            category.setName(request.getName());
        if (request.getDescription() != null)
            category.setDescription(request.getDescription());
        if (request.getSlug() != null)
            category.setSlug(request.getSlug());
        if (request.getIsActive() != null)
            category.setIsActive(request.getIsActive());
        if (request.getSortOrder() != null)
            category.setSortOrder(request.getSortOrder());

        if (request.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(request.getParentCategoryId()).orElse(null);
            category.setParentCategory(parent);
        }
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .isActive(category.getIsActive())
                .sortOrder(category.getSortOrder())
                .parentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .parentCategoryName(
                        category.getParentCategory() != null ? category.getParentCategory().getName() : null)
                .primaryImageUrl(category.getPrimaryImageUrl())
                .images(toImageResponseList(category.getImages()))
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    private CategoryResponse.CategoryImageResponse toImageResponse(CategoryImage image) {
        return CategoryResponse.CategoryImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .isPrimary(image.getIsPrimary())
                .altText(image.getAltText())
                .sortOrder(image.getSortOrder())
                .build();
    }

    private List<CategoryResponse.CategoryImageResponse> toImageResponseList(List<CategoryImage> images) {
        if (images == null)
            return List.of();
        return images.stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}