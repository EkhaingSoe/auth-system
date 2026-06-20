// src/main/java/com/example/auth_system/store/repository/StoreRepository.java
package com.example.auth_system.store.repository;

import com.example.auth_system.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByStoreCode(String storeCode);

    boolean existsByStoreCode(String storeCode);

    List<Store> findByStatus(String status);

    List<Store> findByStoreType(String storeType);

    List<Store> findByParentStoreId(UUID parentStoreId);

    @Query("SELECT s FROM Store s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.storeCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Store> searchStores(@Param("searchTerm") String searchTerm);

    List<Store> findByStatusAndStoreType(String status, String storeType);

    @Query("SELECT s FROM Store s WHERE s.parentStore IS NULL")
    List<Store> findHeadOffices();

    @Query("SELECT s FROM Store s WHERE s.parentStore.id = :parentId")
    List<Store> findChildStores(@Param("parentId") UUID parentId);
}