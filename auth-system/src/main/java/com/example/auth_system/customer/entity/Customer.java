package com.example.auth_system.customer.entity;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.customer.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "customer_code", unique = true, nullable = false, length = 20)
    private String customerCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_group_id")
    private CustomerGroup customerGroup;

    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(length = 100)
    private String country;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "tax_number", length = 50)
    private String taxNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "total_spent")
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "loyalty_points")
    @Builder.Default
    private Integer loyaltyPoints = 0;

    @Column(name = "order_count")
    @Builder.Default
    private Integer orderCount = 0;

    @Column(name = "last_purchase_date")
    private LocalDateTime lastPurchaseDate;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_vip")
    @Builder.Default
    private Boolean isVip = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Wishlist> wishlistItems = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addWishlistItem(Wishlist wishlistItem) {
        wishlistItems.add(wishlistItem);
        wishlistItem.setCustomer(this);
    }

    public void removeWishlistItem(Wishlist wishlistItem) {
        wishlistItems.remove(wishlistItem);
        wishlistItem.setCustomer(null);
    }

    public void updateSpending(BigDecimal amount) {
        if (this.totalSpent == null) {
            this.totalSpent = BigDecimal.ZERO;
        }
        this.totalSpent = this.totalSpent.add(amount);
        this.orderCount = (this.orderCount == null ? 0 : this.orderCount) + 1;
        this.lastPurchaseDate = LocalDateTime.now();
    }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints = (this.loyaltyPoints == null ? 0 : this.loyaltyPoints) + points;
    }

    public void redeemLoyaltyPoints(int points) {
        if (this.loyaltyPoints == null || this.loyaltyPoints < points) {
            throw new IllegalStateException("Insufficient loyalty points");
        }
        this.loyaltyPoints -= points;
    }
}