package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVariantImageRequest {

    private UUID id;

    private UUID variantId;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    private String publicId;

    private Boolean isPrimary;

    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;

    private Integer sortOrder;

    private Boolean isActive;
}