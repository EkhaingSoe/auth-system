package com.example.auth_system.category.service;

import com.example.auth_system.category.dto.request.CreateCategoryRequest;
import com.example.auth_system.category.dto.request.UpdateCategoryRequest;
import com.example.auth_system.category.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

        // ============================================================
        // ADMIN - CATEGORY MANAGEMENT
        // ============================================================

        CategoryResponse createCategory(CreateCategoryRequest request);

        Page<CategoryResponse> getAllCategories(Pageable pageable);

        CategoryResponse getCategoryById(UUID id);

        CategoryResponse getCategoryBySlug(String slug);

        CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request);

        void deleteCategory(UUID id);

        Page<CategoryResponse> searchCategories(String term, Pageable pageable);

        // ============================================================
        // ADMIN - CATEGORY HIERARCHY
        // ============================================================

        List<CategoryResponse> getActiveRootCategories();

        List<CategoryResponse> getActiveSubCategories(UUID parentId);

        Page<CategoryResponse> getCategoriesByStatus(Boolean isActive, Pageable pageable);

        // ============================================================
        // ADMIN - IMAGE MANAGEMENT
        // ============================================================

        CategoryResponse uploadCategoryImage(UUID categoryId, MultipartFile file, Boolean isPrimary);

        CategoryResponse removeCategoryImage(UUID categoryId, UUID imageId);

        CategoryResponse setPrimaryImage(UUID categoryId, UUID imageId);

        // ============================================================
        // E-COMMERCE - CATEGORY
        // ============================================================

        List<CategoryResponse> getEcommerceCategories();

        List<CategoryResponse> getEcommerceRootCategories();

        List<CategoryResponse> getEcommerceSubCategories(UUID parentId);

        CategoryResponse getActiveCategoryBySlug(String slug);
}