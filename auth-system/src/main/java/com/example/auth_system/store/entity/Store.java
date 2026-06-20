// src/main/java/com/example/auth_system/store/entity/Store.java
package com.example.auth_system.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "store_code", unique = true, nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String name;

    private String address;
    private String phone;
    private String email;

    @Column(name = "store_type")
    private String storeType; // HEAD_OFFICE, BRANCH, WAREHOUSE

    private String status; // ACTIVE, INACTIVE, SUSPENDED, CLOSED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_store_id")
    private Store parentStore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB")
    private JsonNode settings;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "tax_number")
    private String taxNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}