package com.example.auth_system.category.repository;

import com.example.auth_system.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findBySlug(String slug);

    List<Category> findByIsActiveTrue();

    List<Category> findByParentCategoryId(UUID parentId);

    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL AND c.isActive = true ORDER BY c.sortOrder")
    List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parentCategory.id = :parentId AND c.isActive = true ORDER BY c.sortOrder")
    List<Category> findActiveSubCategories(@Param("parentId") UUID parentId);

    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Category c ORDER BY c.sortOrder, c.name")
    List<Category> findAllOrdered();
}
