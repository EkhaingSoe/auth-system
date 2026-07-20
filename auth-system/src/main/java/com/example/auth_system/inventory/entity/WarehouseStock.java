package com.example.auth_system.inventory.entity;

import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
})
@Data
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

    @Column(name = "current_quantity")
    @Builder.Default
    private Integer currentQuantity = 0;

    @Column(name = "reserved_quantity")
    @Builder.Default
    private Integer reservedQuantity = 0;

    @Column(name = "min_stock")
    @Builder.Default
    private Integer minStock = 0;

    @Column(name = "max_stock")
    @Builder.Default
    private Integer maxStock = 0;

    @Column(name = "reorder_level")
    @Builder.Default
    private Integer reorderLevel = 0;

    @Column(name = "reorder_quantity")
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

    public Integer getAvailableQuantity() {
        return currentQuantity - reservedQuantity;
    }

    public boolean isBelowReorderLevel() {
        return currentQuantity <= reorderLevel;
    }

    public boolean isOverMaxStock() {
        return currentQuantity > maxStock;
    }
}