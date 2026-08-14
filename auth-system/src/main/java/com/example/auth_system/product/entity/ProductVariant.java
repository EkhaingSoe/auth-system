package com.example.auth_system.product.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_variants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // many variant belong to one product .

    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @Column(unique = true, length = 100)
    private String barcode;

    @Column(name = "selling_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "cost_price", precision = 15, scale = 2)
    private BigDecimal costPrice;

    @Builder.Default
    @Column(length = 3, nullable = false)
    private String currency = "MMK";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attribute_values", columnDefinition = "jsonb")
    private JsonNode attributeValues;

    @Column(precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(precision = 10, scale = 3)
    private BigDecimal length;

    @Column(precision = 10, scale = 3)
    private BigDecimal width;

    @Column(precision = 10, scale = 3)
    private BigDecimal height;

    @Builder.Default
    @Column(length = 20, nullable = false)
    private String unit = "piece";

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addImage(ProductImage image) {
        images.add(image);
        image.setVariant((this));
        image.setProduct(null);
    }

    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setVariant(null);
    }

    public ProductImage getPrimaryImage() {
        return images.stream()
                .filter(image -> Boolean.TRUE.equals(image.getIsPrimary()))
                .findFirst()
                .orElse(null);
    }

    public List<ProductImage> getActiveImages() {
        return images.stream()
                .filter(image -> Boolean.TRUE.equals(image.getIsActive()))
                .toList();
    }

    public long getActiveImageCount() {
        return images.stream()
                .filter(image -> Boolean.TRUE.equals(image.getIsActive()))
                .count();
    }

    public boolean hasImages() {
        return !images.isEmpty();
    }

    public boolean hasActiveImages() {
        return getActiveImageCount() > 0;
    }

}