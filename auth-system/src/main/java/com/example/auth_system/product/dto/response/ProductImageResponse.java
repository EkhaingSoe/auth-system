package com.example.auth_system.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {

    private UUID id;

    private String imageUrl;

    private String publicId;

    private Boolean isPrimary;

    private String altText;

    private Integer sortOrder;
}