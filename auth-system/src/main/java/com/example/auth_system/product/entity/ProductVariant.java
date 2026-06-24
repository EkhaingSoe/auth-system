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
    private Product product;

    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @Column(unique = true, length = 100)
    private String barcode;

    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(length = 3)
    private String currency = "MMK";

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;

    @Column(name = "min_stock_quantity")
    private Integer minStockQuantity = 0;

    @Column(name = "max_stock_quantity")
    private Integer maxStockQuantity = 0;

    @Column(name = "reorder_level")
    private Integer reorderLevel = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attribute_values", columnDefinition = "jsonb")
    private JsonNode attributeValues;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "length")
    private BigDecimal length;

    @Column(name = "width")
    private BigDecimal width;

    @Column(name = "height")
    private BigDecimal height;

    @Column(length = 20)
    private String unit = "piece";

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getAvailableQuantity() {
        return stockQuantity - reservedQuantity;
    }

    public boolean isInStock() {
        return getAvailableQuantity() > 0;
    }

    public boolean needsReorder() {
        return stockQuantity <= reorderLevel;
    }

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
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .orElse(null);
    }

    public List<ProductImage> getActiveImages() {
        return images.stream()
                .filter(image -> image.getIsActive() != null && image.getIsActive())
                .collect(java.util.stream.Collectors.toList());
    }

    public void clearImages() {
        images.clear();
    }

    public long getActiveImageCount() {
        return images.stream()
                .filter(image -> image.getIsActive() != null && image.getIsActive())
                .count();
    }

    public boolean hasImages() {
        return !images.isEmpty();
    }

    public boolean hasActiveImages() {
        return getActiveImageCount() > 0;
    }

}