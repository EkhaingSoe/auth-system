package com.example.auth_system.order.refund.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.auth_system.order.refund.entity.RefundItem;

public interface RefundItemRepository extends JpaRepository<RefundItem, UUID> {

    List<RefundItem> findByRefundId(UUID refundId);

    List<RefundItem> findByOrderItemId(UUID orderItemId);

    List<RefundItem> findByProductId(UUID productId);

    List<RefundItem> findByVariantId(UUID variantId);

    List<RefundItem> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end);

    boolean existsByOrderItemId(UUID orderItemId);

    @Query("""
            SELECT COALESCE(SUM(ri.refundQuantity),0)
            FROM RefundItem ri
            WHERE ri.orderItem.id = :orderItemId
            """)
    Integer getRefundedQuantity(@Param("orderItemId") UUID orderItemId);

    @Query("""
            SELECT COALESCE(SUM(ri.refundAmount),0)
            FROM RefundItem ri
            WHERE ri.refund.id = :refundId
            """)
    BigDecimal getTotalRefundAmount(@Param("refundId") UUID refundId);

    @Query("""
            SELECT
                ri.product.id,
                SUM(ri.refundQuantity)
            FROM RefundItem ri
            GROUP BY ri.product.id
            ORDER BY SUM(ri.refundQuantity) DESC
            """)
    List<Object[]> findMostRefundedProducts();

    @Query("""
            SELECT
                ri.product.id,
                SUM(ri.refundAmount)
            FROM RefundItem ri
            GROUP BY ri.product.id
            """)
    List<Object[]> getRefundAmountByProduct();

}
