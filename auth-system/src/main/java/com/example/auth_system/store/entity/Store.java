// src/main/java/com/example/auth_system/store/entity/Store.java
package com.example.auth_system.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.example.auth_system.store.enums.StoreStatus;
import com.example.auth_system.store.enums.StoreType;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stores", indexes = {
        @Index(name = "idx_store_code", columnList = "store_code"),
        @Index(name = "idx_store_status", columnList = "status"),
        @Index(name = "idx_store_type", columnList = "store_type"),
        @Index(name = "idx_parent_store", columnList = "parent_store_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "store_code", unique = true, nullable = false, length = 50)
    private String storeCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String address;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_type", nullable = false, length = 30)
    private StoreType storeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StoreStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_store_id")
    private Store parentStore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB")
    private JsonNode settings;

    @Column(name = "contact_person", length = 150)
    private String contactPerson;

    @Column(name = "tax_number", length = 100)
    private String taxNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}