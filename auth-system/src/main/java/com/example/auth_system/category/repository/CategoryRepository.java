package com.example.auth_system.category.repository;

import com.example.auth_system.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

        Optional<Category> findBySlug(String slug);

        Optional<Category> findByName(String name);

        boolean existsByName(String name);

        boolean existsBySlug(String slug);

        Page<Category> findAll(Pageable pageable);

        Page<Category> findByNameContainingIgnoreCase(
                        String name,
                        Pageable pageable);

        Page<Category> findByIsActiveTrue(Boolean isActive, Pageable pageable);

        @Query("""
                        SELECT c
                        FROM Category c
                        WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
                           OR LOWER(c.description) LIKE LOWER(CONCAT('%', :term, '%'))
                           OR LOWER(c.slug) LIKE LOWER(CONCAT('%', :term, '%'))
                        """)
        Page<Category> searchCategories(
                        @Param("term") String term,
                        Pageable pageable);

        List<Category> findByParentCategoryIdOrderBySortOrderAscNameAsc(
                        UUID parentId);

        @Query("""
                        SELECT c
                        FROM Category c
                        ORDER BY c.sortOrder ASC, c.name ASC
                        """)
        List<Category> findAllOrdered();

        // ecommerce

        List<Category> findByIsActiveTrueOrderBySortOrderAscNameAsc();

        @Query("""
                        SELECT c
                        FROM Category c
                        WHERE c.parentCategory IS NULL
                          AND c.isActive = true
                        ORDER BY c.sortOrder ASC, c.name ASC
                        """)
        List<Category> findActiveRootCategories();

        @Query("""
                        SELECT c
                        FROM Category c
                        WHERE c.parentCategory.id = :parentId
                          AND c.isActive = true
                        ORDER BY c.sortOrder ASC, c.name ASC
                        """)
        List<Category> findActiveSubCategories(
                        @Param("parentId") UUID parentId);

        Optional<Category> findBySlugAndIsActiveTrue(String slug);

}
