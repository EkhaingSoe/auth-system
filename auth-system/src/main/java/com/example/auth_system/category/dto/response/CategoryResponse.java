package com.example.auth_system.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private String slug;
    private Boolean isActive;
    private Integer sortOrder;
    private UUID parentCategoryId;
    private String parentCategoryName;
    private String primaryImageUrl;
    private List<CategoryImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryImageResponse {
        private UUID id;
        private String imageUrl;
        private Boolean isPrimary;
        private String altText;
        private Integer sortOrder;
    }
}
