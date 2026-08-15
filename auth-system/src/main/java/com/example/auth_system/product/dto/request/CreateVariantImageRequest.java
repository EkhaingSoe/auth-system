package com.example.auth_system.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateVariantImageRequest {

    @NotNull(message = "Variant ID is required")
    private UUID variantId;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @Size(max = 255, message = "Public ID must not exceed 255 characters")
    private String publicId;

    @Builder.Default
    private Boolean isPrimary = false;

    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;

    @Builder.Default
    private Integer sortOrder = 0;
}