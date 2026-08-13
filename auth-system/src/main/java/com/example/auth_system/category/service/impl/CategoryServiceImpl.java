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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryImageRepository categoryImageRepository;
    private final CategoryMapper categoryMapper;
    private final CloudinaryService cloudinaryService;

    // admin
    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw BusinessException.duplicateCategoryName(request.getName());
        }
        String slug = generateSlug(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw BusinessException.duplicateCategorySlug(slug);
        }
        Category category = categoryMapper.toEntity(request);
        category.setSlug(slug);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {

        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " +
                        id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " +
                        id));

        if (request.getName() != null
                && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw BusinessException.duplicateCategoryName(
                        request.getName());
            }
            category.setName(request.getName());
            String slug = generateSlug(request.getName());
            category.setSlug(slug);
        }

        categoryMapper.updateEntity(category, request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id));

        for (CategoryImage image : category.getImages()) {

            if (image.getPublicId() == null || image.getPublicId().isBlank()) {
                continue;
            }

            try {
                cloudinaryService.deleteImage(image.getPublicId());

                log.info(
                        "Deleted category image from Cloudinary: {}",
                        image.getPublicId());

            } catch (IOException e) {

                log.warn(
                        "Failed to delete category image from Cloudinary: {}",
                        image.getPublicId(),
                        e);
            }
        }

        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> searchCategories(String term, Pageable pageable) {

        return categoryRepository.searchCategories(term, pageable)
                .map(categoryMapper::toResponse);
    }

    // // ============================================================
    // // CATEGORY HIERARCHY
    // // ============================================================

    @Override
    public List<CategoryResponse> getActiveRootCategories() {
        log.info("Getting root categories");
        return categoryMapper.toResponseList(categoryRepository.findActiveRootCategories());
    }

    @Override
    public List<CategoryResponse> getActiveSubCategories(UUID parentId) {
        log.info("Getting sub-categories for parent: {}", parentId);
        return categoryMapper.toResponseList(categoryRepository.findActiveSubCategories(parentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategoriesByStatus(
            Boolean isActive,
            Pageable pageable) {

        log.info("Getting categories by status: {}", isActive);

        return categoryRepository.findByIsActiveTrue(isActive, pageable)
                .map(categoryMapper::toResponse);
    }

    // // ============================================================
    // // IMAGE MANAGEMENT
    // // ============================================================

    @Override
    public CategoryResponse uploadCategoryImage(UUID categoryId, MultipartFile file, Boolean isPrimary) {
        log.info("Uploading image for category: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

        if (file == null || file.isEmpty()) {
            throw new BusinessException("file", "Category image is required", "CATEGORY_IMAGE_REQUIRED",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(file, "categories");

            String imageUrl = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            if (isPrimary != null && isPrimary) {
                categoryImageRepository.removePrimaryFlag(categoryId);
            }

            CategoryImage image = CategoryImage.builder()
                    .category(category)
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .isPrimary(isPrimary != null ? isPrimary : false)
                    .altText(file.getOriginalFilename())
                    .sortOrder(category.getImages().size())
                    .build();

            categoryImageRepository.save(image);
            category.addImage(image);
            return categoryMapper.toResponse(category);

        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new BusinessException(
                    "file",
                    "Failed to upload category image",
                    "CATEGORY_IMAGE_UPLOAD_FAILED",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CategoryResponse removeCategoryImage(UUID categoryId, UUID imageId) {
        log.info("Removing image: {} from category: {}", imageId, categoryId);

        CategoryImage image = categoryImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " +
                        imageId));

        if (!image.getCategory().getId().equals(categoryId)) {
            throw new BusinessException("imageId", "Image does not belong to this category", "CATEGORY_IMAGE_MISMATCH",
                    HttpStatus.BAD_REQUEST);
        }
        boolean wasPrimary = Boolean.TRUE.equals(image.getIsPrimary());

        // Delete from Cloudinary
        if (image.getPublicId() != null && !image.getPublicId().isBlank()) {

            try {
                cloudinaryService.deleteImage(image.getPublicId());
                log.info("Deleted image from Cloudinary: {}", image.getPublicId());

            } catch (IOException e) {
                log.warn("Failed to delete image from Cloudinary: {}", image.getPublicId(), e);
            }
        }

        categoryImageRepository.delete(image);

        // If deleted image was primary, set another as primary
        if (wasPrimary) {
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

        CategoryImage image = categoryImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + imageId));

        if (!image.getCategory().getId().equals(categoryId)) {
            throw new BusinessException("imageId", "Image does not belong to this category", "CATEGORY_IMAGE_MISMATCH",
                    HttpStatus.BAD_REQUEST);
        }

        categoryImageRepository.removePrimaryFlag(categoryId);

        image.setIsPrimary(true);
        categoryImageRepository.save(image);
        return getCategoryById(categoryId);
    }

    // ============================================================
    // E-COMMERCE - CATEGORY
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getEcommerceCategories() {

        log.info("Getting all active e-commerce categories");

        return categoryRepository
                .findByIsActiveTrueOrderBySortOrderAscNameAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getEcommerceRootCategories() {

        log.info("Getting active root categories for e-commerce");

        return categoryRepository
                .findActiveRootCategories()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getEcommerceSubCategories(UUID parentId) {

        log.info("Getting active sub-categories for parent: {}", parentId);

        return categoryRepository
                .findActiveSubCategories(parentId)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getActiveCategoryBySlug(String slug) {

        log.info("Getting active category by slug: {}", slug);

        Category category = categoryRepository
                .findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active category not found with slug: "
                                + slug));

        return categoryMapper.toResponse(category);
    }

    private String generateSlug(String name) {
        return name
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

}