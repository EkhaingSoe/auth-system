package com.example.auth_system.inventory.entity;

import java.util.UUID;

import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory_count_items", indexes = {
        @Index(name = "idx_inventory_count_item_count", columnList = "inventory_count_id"),
        @Index(name = "idx_inventory_count_item_product", columnList = "product_id"),
        @Index(name = "idx_inventory_count_item_variant", columnList = "variant_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCountItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_count_id")
    private InventoryCount inventoryCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(name = "system_quantity", nullable = false)
    private Integer systemQuantity;

    @Column(name = "counted_quantity", nullable = false)
    private Integer countedQuantity;

    @Column(name = "difference", nullable = false)
    private Integer difference;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_adjustment_id")
    private StockAdjustment stockAdjustment;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
