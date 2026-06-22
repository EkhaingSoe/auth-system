package com.example.auth_system.category.repository;

import com.example.auth_system.category.entity.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, UUID> {

    List<CategoryImage> findByCategoryIdOrderBySortOrder(UUID categoryId);

    @Query("SELECT ci FROM CategoryImage ci WHERE ci.category.id = :categoryId AND ci.isPrimary = true")
    CategoryImage findPrimaryImage(@Param("categoryId") UUID categoryId);

    @Modifying
    @Transactional
    @Query("UPDATE CategoryImage ci SET ci.isPrimary = false WHERE ci.category.id = :categoryId")
    void removePrimaryFlag(@Param("categoryId") UUID categoryId);

    void deleteByCategoryId(UUID categoryId);
}