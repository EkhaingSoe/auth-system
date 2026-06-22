package com.example.auth_system.category.service.impl;

import com.example.auth_system.category.dto.request.CreateCategoryRequest;
import com.example.auth_system.category.dto.request.UpdateCategoryRequest;
import com.example.auth_system.category.dto.response.CategoryResponse;
import com.example.auth_system.category.entity.Category;
import com.example.auth_system.category.entity.CategoryImage;
import com.example.auth_system.category.mapper.CategoryMapper;
import com.example.auth_system.category.repository.CategoryImageRepository;
import com.example.auth_system.category.repository.CategoryRepository;
import com.example.auth_system.category.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryImageRepository categoryImageRepository;
    private final CategoryMapper categoryMapper;
    private final CloudinaryService cloudinaryService;

    // ============================================================
    // CATEGORY CRUD
    // ============================================================

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating category: {}", request.getName());

        // if (categoryRepository.existsByName(request.getName())) {
        // throw new RuntimeException("Category with name '" + request.getName() + "'
        // already exists");
        // }

        if (request.getSlug() != null && categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists: " + request.getSlug());
        }

        Category category = categoryMapper.toEntity(request);
        category = categoryRepository.save(category);

        log.info("Category created: {} with slug: {}", category.getId(), category.getSlug());
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Getting all categories");
        return categoryMapper.toResponseList(categoryRepository.findAllOrdered());
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        log.info("Getting category by id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        log.info("Getting category by slug: {}", slug);
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request) {
        log.info("Updating category: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));

        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug())) {
            if (categoryRepository.existsBySlug(request.getSlug())) {
                throw new RuntimeException("Slug already exists: " + request.getSlug());
            }
        }

        categoryMapper.updateEntity(category, request);
        category = categoryRepository.save(category);

        log.info("Category updated: {}", id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        log.info("Deleting category: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));

        // Delete images from Cloudinary
        for (CategoryImage image : category.getImages()) {
            try {
                cloudinaryService.deleteImage(image.getPublicId());
            } catch (IOException e) {
                log.warn("Failed to delete image from Cloudinary: {}", e.getMessage());
            }
        }

        categoryRepository.delete(category);
        log.info("Category deleted: {}", id);
    }

    // ============================================================
    // CATEGORY HIERARCHY
    // ============================================================

    @Override
    public List<CategoryResponse> getRootCategories() {
        log.info("Getting root categories");
        return categoryMapper.toResponseList(categoryRepository.findRootCategories());
    }

    @Override
    public List<CategoryResponse> getSubCategories(UUID parentId) {
        log.info("Getting sub-categories for parent: {}", parentId);
        return categoryMapper.toResponseList(categoryRepository.findActiveSubCategories(parentId));
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {
        log.info("Getting active categories");
        return categoryMapper.toResponseList(categoryRepository.findByIsActiveTrue());
    }

    // ============================================================
    // IMAGE MANAGEMENT
    // ============================================================

    @Override
    public CategoryResponse uploadCategoryImage(UUID categoryId, MultipartFile file, Boolean isPrimary) {
        log.info("Uploading image for category: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

        try {
            // Upload to Cloudinary
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(file, "categories");

            String imageUrl = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            // If this is primary, remove primary flag from other images
            if (isPrimary != null && isPrimary) {
                categoryImageRepository.removePrimaryFlag(categoryId);
            }

            // Create image entity
            CategoryImage image = CategoryImage.builder()
                    .category(category)
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .isPrimary(isPrimary != null ? isPrimary : false)
                    .altText(file.getOriginalFilename())
                    .sortOrder(category.getImages().size())
                    .build();

            image = categoryImageRepository.save(image);
            category.addImage(image);

            log.info("Image uploaded for category: {}", categoryId);
            return categoryMapper.toResponse(category);

        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public CategoryResponse removeCategoryImage(UUID categoryId, UUID imageId) {
        log.info("Removing image: {} from category: {}", imageId, categoryId);

        CategoryImage image = categoryImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + imageId));

        if (!image.getCategory().getId().equals(categoryId)) {
            throw new RuntimeException("Image does not belong to this category");
        }

        // Delete from Cloudinary
        try {
            cloudinaryService.deleteImage(image.getPublicId());
        } catch (IOException e) {
            log.warn("Failed to delete from Cloudinary: {}", e.getMessage());
        }

        categoryImageRepository.delete(image);

        // If deleted image was primary, set another as primary
        if (image.getIsPrimary()) {
            List<CategoryImage> remainingImages = categoryImageRepository.findByCategoryIdOrderBySortOrder(categoryId);
            if (!remainingImages.isEmpty()) {
                CategoryImage newPrimary = remainingImages.get(0);
                newPrimary.setIsPrimary(true);
                categoryImageRepository.save(newPrimary);
            }
        }

        log.info("Image removed: {}", imageId);
        return getCategoryById(categoryId);
    }

    @Override
    public CategoryResponse setPrimaryImage(UUID categoryId, UUID imageId) {
        log.info("Setting primary image: {} for category: {}", imageId, categoryId);

        // Remove primary flag from all images
        categoryImageRepository.removePrimaryFlag(categoryId);

        // Set new primary
        CategoryImage image = categoryImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + imageId));

        if (!image.getCategory().getId().equals(categoryId)) {
            throw new RuntimeException("Image does not belong to this category");
        }

        image.setIsPrimary(true);
        categoryImageRepository.save(image);

        log.info("Primary image set: {}", imageId);
        return getCategoryById(categoryId);
    }
}