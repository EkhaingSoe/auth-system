package com.example.auth_system.inventory.entity;

import java.util.UUID;

import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_count_items")
@Data
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

    private Integer systemQuantity;

    private Integer countedQuantity;

    private Integer difference;

    private String notes;
}
