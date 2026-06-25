// product/repository/ProductWarehouseRepository.java
package com.example.auth_system.product.repository;

import com.example.auth_system.product.entity.ProductWarehouse;
import com.example.auth_system.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, UUID> {

    List<ProductWarehouse> findByProductId(UUID productId);

    List<ProductWarehouse> findByWarehouseId(UUID warehouseId);

    Optional<ProductWarehouse> findByProductIdAndWarehouseId(UUID productId, UUID warehouseId);

    @Query("SELECT pw.warehouse FROM ProductWarehouse pw WHERE pw.product.id = :productId")
    List<Store> findStoresByProductId(@Param("productId") UUID productId);

    @Query("SELECT SUM(pw.stockQuantity) FROM ProductWarehouse pw WHERE pw.product.id = :productId")
    Integer getTotalStockByProductId(@Param("productId") UUID productId);

    @Query("SELECT SUM(pw.stockQuantity) FROM ProductWarehouse pw WHERE pw.product.id = :productId AND pw.warehouse.id = :warehouseId")
    Integer getStockByProductAndWarehouse(@Param("productId") UUID productId, @Param("warehouseId") UUID warehouseId);
}