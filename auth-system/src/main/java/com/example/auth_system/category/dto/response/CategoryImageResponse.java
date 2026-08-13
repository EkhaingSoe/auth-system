package com.example.auth_system.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryImageResponse {

    private UUID id;
    private String imageUrl;
    private Boolean isPrimary;
    private String altText;
    private Integer sortOrder;
}