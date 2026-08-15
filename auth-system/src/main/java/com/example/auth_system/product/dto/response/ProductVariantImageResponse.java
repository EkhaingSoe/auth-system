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
public class ProductVariantImageResponse {

    private UUID id;

    private UUID variantId;

    private String imageUrl;

    private String publicId;

    private Boolean isPrimary;

    private String altText;

    private Integer sortOrder;
}
