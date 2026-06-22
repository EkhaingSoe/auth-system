// src/main/java/com/example/auth_system/common/service/CloudinaryService.java
package com.example.auth_system.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder:categories}")
    private String folderName;

    // upload image to cloudiary
    public Map<String, Object> uploadImage(MultipartFile file, String subFolder) throws IOException {
        return uploadImage(file, subFolder, null);
    }

    // Upload image to Cloudinary with optional transformation

    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadImage(MultipartFile file, String subFolder, Map<String, Object> options)
            throws IOException {
        log.info("Uploading image to Cloudinary: {}", file.getOriginalFilename());

        String publicId = generatePublicId(subFolder, file.getOriginalFilename()); // "categories/550e8400-e29b-41d4-a716-446655440000.jpg"

        // Use HashMap for type safety
        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", publicId);
        uploadParams.put("folder", folderName + "/" + subFolder);
        uploadParams.put("resource_type", "image");
        uploadParams.put("overwrite", true);

        if (options != null) {
            uploadParams.putAll(options);
        }

        // Add transformation using Transformation object
        Transformation transformation = new Transformation()
                .width(800)
                .height(800)
                .crop("limit")
                .quality("auto")
                .fetchFormat("auto");

        uploadParams.put("transformation", transformation);

        try {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            log.info("Image uploaded successfully: {}", result.get("public_id"));
            return result;
        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new IOException("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Delete image from Cloudinary
     */
    public Map<String, Object> deleteImage(String publicId) throws IOException {
        log.info("Deleting image: {}", publicId);
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Image deleted successfully");
            return result;
        } catch (IOException e) {
            log.error("Failed to delete image: {}", e.getMessage());
            throw new IOException("Failed to delete image from Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Get image URL with transformations
     */
    public String getImageUrl(String publicId, int width, int height) {
        // Use Transformation object
        Transformation transformation = new Transformation()
                .width(width)
                .height(height)
                .crop("fill")
                .quality("auto");

        return cloudinary.url()
                .transformation(transformation)
                .generate(publicId);
    }

    /**
     * Get thumbnail URL (200x200)
     */
    public String getThumbnailUrl(String publicId) {
        return getImageUrl(publicId, 200, 200);
    }

    /**
     * Get optimized image URL
     */
    public String getOptimizedUrl(String publicId) {
        Transformation transformation = new Transformation()
                .quality("auto")
                .fetchFormat("auto")
                .width(800)
                .crop("limit");

        return cloudinary.url()
                .transformation(transformation)
                .generate(publicId);
    }

    private String generatePublicId(String subFolder, String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return subFolder + "/" + UUID.randomUUID() + extension; // "categories/550e8400-e29b-41d4-a716-446655440000.jpg"
    }

    /**
     * Extract public ID from Cloudinary URL
     */
    public String extractPublicId(String imageUrl) {
        if (imageUrl == null)
            return null;

        try {
            // Example:
            // https://res.cloudinary.com/cloud/image/upload/v123/categories/electronics/abc123.jpg
            // Returns: categories/electronics/abc123
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                // Remove version prefix if exists: v123/
                if (path.startsWith("v")) {
                    int slashIndex = path.indexOf("/");
                    if (slashIndex > 0) {
                        path = path.substring(slashIndex + 1);
                    }
                }
                int lastDot = path.lastIndexOf(".");
                if (lastDot > 0) {
                    return path.substring(0, lastDot);
                }
                return path;
            }
        } catch (Exception e) {
            log.warn("Could not extract public ID from URL: {}", imageUrl);
        }
        return null;
    }

    /**
     * Check if image exists in Cloudinary
     */
    public boolean imageExists(String publicId) {
        try {
            Map<String, Object> result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return result != null && !result.isEmpty();
        } catch (Exception e) {
            log.warn("Image not found: {}", publicId);
            return false;
        }
    }
}