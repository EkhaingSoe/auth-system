package com.example.auth_system.inventory.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CurrentUserService;
import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.CreateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountResponse;
import com.example.auth_system.inventory.entity.InventoryCount;
import com.example.auth_system.inventory.entity.InventoryCountItem;
import com.example.auth_system.inventory.entity.StockAdjustment;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.enums.AdjustmentStatus;
import com.example.auth_system.inventory.enums.CountType;
import com.example.auth_system.inventory.enums.InventoryCountStatus;
import com.example.auth_system.inventory.mapper.InventoryCountMapper;
import com.example.auth_system.inventory.repository.InventoryCountRepository;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.InventoryCountService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryCountServiceImpl implements InventoryCountService {

    private final StoreRepository storeRepository;
    private final CurrentUserService currentUserService;
    private final InventoryCountRepository inventoryCountRepository;
    private final InventoryCountMapper inventoryCountMapper;
    private final WarehouseStockRepository warehouseStockRepository;

    @Override
    @Transactional
    public InventoryCountResponse create(CreateInventoryCountRequest request) {

        Store warehouse = storeRepository.findById(request.getWarehouseId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Warehouse not found"));

        String countNumber = generateInventoryCount();
        User currentUser = currentUserService.getCurrentUser();

        InventoryCount count = InventoryCount.builder()
                .countNumber(countNumber)
                .warehouse(warehouse)
                .countType(request.getCountType())
                .status(InventoryCountStatus.PENDING)
                .scheduledDate(request.getScheduledDate())
                .notes(request.getNotes())
                .createdBy(currentUser)
                .build();

        inventoryCountRepository.save(count);

        if (request.getCountType() == CountType.FULL_COUNT) {

            createFullCountItems(count);

        } else if (request.getCountType() == CountType.CYCLE_COUNT) {

            // createPartialCountItems(count, request.getItems());

        }

        return inventoryCountMapper.toInventoryCountResponse(count);
    }

    private String generateInventoryCount() {
        return "IC-" + LocalDate.now()
                + "-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }

    private void createFullCountItems(
            InventoryCount count) {

        List<WarehouseStock> stocks = warehouseStockRepository
                .findByWarehouseId(
                        count.getWarehouse().getId());

        for (WarehouseStock stock : stocks) {

            InventoryCountItem item = InventoryCountItem.builder()
                    .inventoryCount(count)
                    .product(stock.getProduct())
                    .variant(stock.getVariant())
                    .systemQuantity(stock.getCurrentQuantity())
                    .build();

            count.addItem(item);
        }
    }
}
