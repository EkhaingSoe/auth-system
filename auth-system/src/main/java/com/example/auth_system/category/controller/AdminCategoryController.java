package com.example.auth_system.category.controller;

import com.example.auth_system.category.dto.request.CreateCategoryRequest;
import com.example.auth_system.category.dto.request.UpdateCategoryRequest;
import com.example.auth_system.category.dto.response.CategoryResponse;
import com.example.auth_system.category.service.CategoryService;
import com.example.auth_system.common.dto.response.ApiResponse;
import com.example.auth_system.common.exception.BusinessException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

        private final CategoryService categoryService;

        @GetMapping
        @PreAuthorize("@permission.hasPermission('CATEGORY_READ')")
        public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategories(
                        @PageableDefault(size = 20, sort = "sortOrder") Pageable pageable) {

                log.info("GET /api/admin/categories - Getting categories");

                Page<CategoryResponse> categories = categoryService.getAllCategories(pageable);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Categories retrieved successfully",
                                                categories));
        }

        @GetMapping("/{id}")
        @PreAuthorize("@permission.hasPermission('CATEGORY_READ')")
        public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
                        @PathVariable UUID id) {

                log.info("GET /api/admin/categories/{} - Getting category", id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Category retrieved successfully",
                                                categoryService.getCategoryById(id)));
        }

        @GetMapping("/slug/{slug}")
        @PreAuthorize("@permission.hasPermission('CATEGORY_READ')")
        public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(
                        @PathVariable String slug) {

                log.info("GET /api/admin/categories/slug/{} - Getting category",
                                slug);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Category retrieved successfully",
                                                categoryService.getCategoryBySlug(slug)));
        }

        @PostMapping
        @PreAuthorize("@permission.hasPermission('CATEGORY_CREATE')")
        public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
                        @Valid @RequestBody CreateCategoryRequest request) {

                log.info("POST /api/admin/categories - Creating category: {}",
                                request.getName());

                CategoryResponse category = categoryService.createCategory(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                201,
                                                                "Category created successfully",
                                                                category));
        }

        @PutMapping("/{id}")
        @PreAuthorize("@permission.hasPermission('CATEGORY_UPDATE')")
        public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateCategoryRequest request) {

                log.info("PUT /api/admin/categories/{} - Updating category", id);

                CategoryResponse category = categoryService.updateCategory(id, request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Category updated successfully",
                                                category));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("@permission.hasPermission('CATEGORY_DELETE')")
        public ResponseEntity<ApiResponse<Void>> deleteCategory(
                        @PathVariable UUID id) {

                log.info("DELETE /api/admin/categories/{} - Deleting category",
                                id);

                categoryService.deleteCategory(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Category deleted successfully",
                                                null));
        }

        @PostMapping("/{categoryId}/images")
        @PreAuthorize("@permission.hasPermission('CATEGORY_UPDATE')")
        public ResponseEntity<ApiResponse<CategoryResponse>> uploadCategoryImage(
                        @PathVariable UUID categoryId,
                        @RequestParam("file") MultipartFile file,
                        @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary) {

                log.info("POST /api/admin/categories/{}/images - Uploading image", categoryId);

                if (file == null || file.isEmpty()) {
                        throw BusinessException.emptyFile();
                }

                CategoryResponse category = categoryService.uploadCategoryImage(categoryId, file, isPrimary);

                return ResponseEntity.ok(ApiResponse.success(
                                200,
                                "Image uploaded successfully",
                                category));
        }

        @DeleteMapping("/{categoryId}/images/{imageId}")
        @PreAuthorize("@permission.hasPermission('CATEGORY_UPDATE')")
        public ResponseEntity<ApiResponse<CategoryResponse>> removeCategoryImage(
                        @PathVariable UUID categoryId,
                        @PathVariable UUID imageId) {

                log.info(
                                "DELETE /api/admin/categories/{}/images/{}",
                                categoryId,
                                imageId);

                CategoryResponse category = categoryService.removeCategoryImage(
                                categoryId,
                                imageId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Image removed successfully",
                                                category));
        }

        // ============================================================
        // ADMIN - SET PRIMARY IMAGE
        // ============================================================

        @PatchMapping("/{categoryId}/images/{imageId}/primary")
        @PreAuthorize("@permission.hasPermission('CATEGORY_UPDATE')")
        public ResponseEntity<ApiResponse<CategoryResponse>> setPrimaryImage(
                        @PathVariable UUID categoryId,
                        @PathVariable UUID imageId) {

                log.info(
                                "PATCH /api/admin/categories/{}/images/{}/primary",
                                categoryId,
                                imageId);

                CategoryResponse category = categoryService.setPrimaryImage(
                                categoryId,
                                imageId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Primary image set successfully",
                                                category));
        }
}