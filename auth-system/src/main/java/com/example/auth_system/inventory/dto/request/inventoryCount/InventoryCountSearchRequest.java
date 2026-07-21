package com.example.auth_system.inventory.dto.request.inventoryCount;

import com.example.auth_system.inventory.enums.CountType;
import com.example.auth_system.inventory.enums.InventoryCountStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCountSearchRequest {

    // Search by count number
    // Example: COUNT-0001
    private String search;

    // Filter by warehouse
    private UUID warehouseId;

    // Filter by status
    // PENDING, IN_PROGRESS, COMPLETED, VERIFIED
    private InventoryCountStatus status;

    // Filter by count type
    // FULL, PARTIAL, CYCLE
    private CountType countType;

    // Date filter
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    // Pagination
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    // Sorting
    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";
}
