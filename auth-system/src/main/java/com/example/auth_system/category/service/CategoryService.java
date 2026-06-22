package com.example.auth_system.category.service;

import com.example.auth_system.category.dto.request.CreateCategoryRequest;
import com.example.auth_system.category.dto.request.UpdateCategoryRequest;
import com.example.auth_system.category.dto.response.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    // Category CRUD
    CategoryResponse createCategory(CreateCategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(UUID id);

    CategoryResponse getCategoryBySlug(String slug);

    CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request);

    void deleteCategory(UUID id);

    // Category Hierarchy
    List<CategoryResponse> getRootCategories();

    List<CategoryResponse> getSubCategories(UUID parentId);

    List<CategoryResponse> getActiveCategories();

    // Image Management
    CategoryResponse uploadCategoryImage(UUID categoryId, MultipartFile file, Boolean isPrimary);

    CategoryResponse removeCategoryImage(UUID categoryId, UUID imageId);

    CategoryResponse setPrimaryImage(UUID categoryId, UUID imageId);
}