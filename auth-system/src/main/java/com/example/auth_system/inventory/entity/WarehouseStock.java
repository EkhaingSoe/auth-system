package com.example.auth_system.inventory.entity;

import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "warehouse_stocks", uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_variant_warehouse", columnNames = {
                "product_id",
                "variant_id",
                "warehouse_id"
        })
}, indexes = {
        @Index(name = "idx_warehouse_stock_product", columnList = "product_id"),
        @Index(name = "idx_warehouse_stock_variant", columnList = "variant_id"),
        @Index(name = "idx_warehouse_stock_warehouse", columnList = "warehouse_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Store warehouse;

    @Column(name = "current_quantity", nullable = false)
    @Builder.Default
    private Integer currentQuantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    @Builder.Default
    private Integer reservedQuantity = 0;

    @Column(name = "min_stock", nullable = false)
    @Builder.Default
    private Integer minStock = 0;

    @Column(name = "max_stock", nullable = false)
    @Builder.Default
    private Integer maxStock = 0;

    @Column(name = "reorder_level", nullable = false)
    @Builder.Default
    private Integer reorderLevel = 0;

    @Column(name = "reorder_quantity", nullable = false)
    @Builder.Default
    private Integer reorderQuantity = 0;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    public Integer getAvailableQuantity() {
        return Math.max(
                0,
                currentQuantity - reservedQuantity);
    }

    @Transient
    public boolean isBelowReorderLevel() {
        return currentQuantity <= reorderLevel;
    }

    @Transient
    public boolean isOverMaxStock() {
        return maxStock > 0
                && currentQuantity > maxStock;
    }

    @Transient
    public boolean isOutOfStock() {
        return currentQuantity <= 0;
    }

    @Transient
    public boolean isLowStock() {
        return currentQuantity > 0
                && currentQuantity <= reorderLevel;
    }

    public void increaseQuantity(Integer quantity) {

        validateQuantity(quantity);

        this.currentQuantity += quantity;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void decreaseQuantity(Integer quantity) {

        validateQuantity(quantity);

        if (this.currentQuantity < quantity) {
            throw new IllegalStateException(
                    "Insufficient stock");
        }

        this.currentQuantity -= quantity;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // ============================================================
    // RESERVATION
    // ============================================================

    public void reserveQuantity(Integer quantity) {

        validateQuantity(quantity);

        if (getAvailableQuantity() < quantity) {
            throw new IllegalStateException(
                    "Insufficient available stock");
        }

        this.reservedQuantity += quantity;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void releaseReservedQuantity(Integer quantity) {

        validateQuantity(quantity);

        if (this.reservedQuantity < quantity) {
            throw new IllegalStateException(
                    "Reserved quantity cannot be negative");
        }

        this.reservedQuantity -= quantity;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private void validateQuantity(Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }
    }

    public boolean hasVariant() {
        return variant != null;
    }
}