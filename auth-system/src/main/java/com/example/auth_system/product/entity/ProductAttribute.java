package com.example.auth_system.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_attributes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "attribute_type", length = 30)
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductAttributeValue> values = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AttributeType {
        color, size, material, style, custom
    }

    public void addValue(ProductAttributeValue value) {
        values.add(value);
        value.setAttribute(this);
    }

    public void removeValue(ProductAttributeValue value) {
        values.remove(value);
        value.setAttribute(null);
    }
}