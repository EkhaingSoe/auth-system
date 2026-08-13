package com.example.auth_system.category.controller;

import com.example.auth_system.category.dto.response.CategoryResponse;
import com.example.auth_system.category.service.CategoryService;
import com.example.auth_system.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

        private final CategoryService categoryService;

        // ============================================================
        // E-COMMERCE - ACTIVE CATEGORIES
        // ============================================================

        @GetMapping
        public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {

                log.info("GET /api/categories - Getting active categories");

                List<CategoryResponse> categories = categoryService.getActiveRootCategories();

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Categories retrieved successfully",
                                                categories));
        }

        // ============================================================
        // E-COMMERCE - ROOT CATEGORIES
        // ============================================================

        @GetMapping("/root")
        public ResponseEntity<ApiResponse<List<CategoryResponse>>> getRootCategories() {

                log.info("GET /api/categories/root - Getting root categories");

                List<CategoryResponse> categories = categoryService.getActiveRootCategories();

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Root categories retrieved successfully",
                                                categories));
        }

        // ============================================================
        // E-COMMERCE - CATEGORY BY SLUG
        // ============================================================

        @GetMapping("/slug/{slug}")
        public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(
                        @PathVariable String slug) {

                log.info(
                                "GET /api/categories/slug/{} - Getting category",
                                slug);

                CategoryResponse category = categoryService.getActiveCategoryBySlug(slug);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Category retrieved successfully",
                                                category));
        }

        // ============================================================
        // E-COMMERCE - SUB CATEGORIES
        // ============================================================

        @GetMapping("/{parentId}/children")
        public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubCategories(
                        @PathVariable UUID parentId) {

                log.info(
                                "GET /api/categories/{}/children",
                                parentId);

                List<CategoryResponse> categories = categoryService.getActiveSubCategories(parentId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                200,
                                                "Sub-categories retrieved successfully",
                                                categories));
        }
}