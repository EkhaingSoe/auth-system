package com.example.auth_system.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryImageRequest {
    private String imageUrl;
    private Boolean isPrimary;
    private String altText;
    private Integer sortOrder;
}