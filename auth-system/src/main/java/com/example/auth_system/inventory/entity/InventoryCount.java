package com.example.auth_system.inventory.entity;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.inventory.enums.CountType;
import com.example.auth_system.inventory.enums.InventoryCountStatus;
import com.example.auth_system.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inventory_counts", indexes = {

        @Index(name = "idx_inventory_count_warehouse", columnList = "warehouse_id"),

        @Index(name = "idx_inventory_count_status", columnList = "status"),

        @Index(name = "idx_inventory_count_created", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "count_number", unique = true, nullable = false, length = 20)
    private String countNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Store warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private InventoryCountStatus status = InventoryCountStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "count_type")
    private CountType countType;

    @Column(name = "count_date")
    private LocalDateTime countDate;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @OneToMany(mappedBy = "inventoryCount", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryCountItem> items = new ArrayList<>(); // [{productId, variantId, expected, counted,
                                                                // difference}]

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User completedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addItem(InventoryCountItem item) {
        items.add(item);
        item.setInventoryCount(this);
    }

    public void removeItem(InventoryCountItem item) {
        items.remove(item);
        item.setInventoryCount(null);
    }
}