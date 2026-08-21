package com.example.auth_system.inventory.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CurrentUserService;
import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateInventoryCountRequest;
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
import com.example.auth_system.inventory.repository.InventoryCountItemRepository;
import com.example.auth_system.inventory.repository.InventoryCountRepository;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.InventoryCountService;
import com.example.auth_system.inventory.service.StockAdjustmentService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.repository.StoreRepository;
import com.example.auth_system.user.entity.User;

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
    private final InventoryCountItemRepository inventoryCountItemRepository;
    private final StockAdjustmentService stockAdjustmentService;

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

        return inventoryCountMapper.toInventoryCountResponse(count);
    }

    @Override
    @Transactional
    public InventoryCountResponse update(UUID countId, UpdateInventoryCountRequest request) {

        InventoryCount count = inventoryCountRepository.findById(countId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory count not found"));

        if (count.getStatus() == InventoryCountStatus.COMPLETED ||
                count.getStatus() == InventoryCountStatus.CANCELLED) {

            throw new BusinessException(
                    "Completed or cancelled inventory count cannot be updated");
        }

        count.setScheduledDate(request.getScheduledDate());
        count.setNotes(request.getNotes());
        InventoryCount updatedCount = inventoryCountRepository.save(count);

        return inventoryCountMapper.toInventoryCountResponse(updatedCount);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryCountResponse getById(UUID countId) {

        InventoryCount count = inventoryCountRepository.findById(countId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory count not found"));

        return inventoryCountMapper.toInventoryCountResponse(count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCountResponse> getAll() {

        List<InventoryCount> counts = inventoryCountRepository.findAll();
        return counts.stream()
                .map(inventoryCountMapper::toInventoryCountResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCountResponse> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        List<InventoryCount> counts = inventoryCountRepository.search(keyword.trim());
        return counts.stream()
                .map(inventoryCountMapper::toInventoryCountResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCountResponse> getByWarehouse(UUID warehouseId) {

        if (warehouseId == null) {
            throw new BusinessException("Warehouse ID is required");
        }

        List<InventoryCount> counts = inventoryCountRepository.findByWarehouseId(warehouseId);
        return counts.stream()
                .map(inventoryCountMapper::toInventoryCountResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCountResponse> getByStatus(InventoryCountStatus status) {

        if (status == null) {
            throw new BusinessException("Status is required");
        }

        List<InventoryCount> counts = inventoryCountRepository.findByStatus(status);
        return counts.stream()
                .map(inventoryCountMapper::toInventoryCountResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public InventoryCountResponse startCount(UUID id) {

        InventoryCount count = inventoryCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory count not found"));

        if (count.getStatus() != InventoryCountStatus.PENDING) {
            throw new BusinessException(
                    "Only pending count can start");
        }

        if (count.getCountType() == CountType.FULL_COUNT) {
            createFullCountItems(count);
        } else if (count.getCountType() == CountType.CYCLE_COUNT) {
            if (count.getItems().isEmpty()) {
                throw new BusinessException(
                        "Please add products before starting count");
            }
        }

        count.setStatus(InventoryCountStatus.IN_PROGRESS);
        inventoryCountRepository.save(count);
        return inventoryCountMapper.toInventoryCountResponse(count);
    }

    @Transactional
    @Override
    public InventoryCountResponse completeCount(UUID countId) {
        InventoryCount count = inventoryCountRepository.findById(countId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory count not found"));

        if (count.getStatus() != InventoryCountStatus.IN_PROGRESS) {
            throw new BusinessException(
                    "Only inventory counts in IN_PROGRESS status can be completed");
        }

        List<InventoryCountItem> items = inventoryCountItemRepository.findByInventoryCountId(count.getId());
        if (items.isEmpty()) {
            throw new BusinessException(
                    "Inventory count has no items");
        }
        // boolean hasUncountedItems = items.stream().anyMatch(item ->
        // item.getCountedQuantity() == null);
        long uncounted = inventoryCountItemRepository.countUncountedItems(countId);

        if (uncounted > 0) {
            throw new BusinessException(
                    "Please count all items before completing inventory count");
        }

        count.setStatus(InventoryCountStatus.COMPLETED);
        count.setCompletedDate(LocalDateTime.now());
        count.setCompletedBy(currentUserService.getCurrentUser());
        InventoryCount savedCount = inventoryCountRepository.save(count);
        return inventoryCountMapper.toInventoryCountResponse(savedCount);
    }

    @Transactional
    @Override
    public InventoryCountResponse cancelCount(UUID countId) {

        InventoryCount count = inventoryCountRepository.findById(countId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory count not found"));

        if (count.getStatus() != InventoryCountStatus.COMPLETED) {
            throw new BusinessException(
                    "completed inventory counts can be cancelled");
        }

        if (count.getStatus() == InventoryCountStatus.CANCELLED) {
            throw new BusinessException(
                    "Inventory count is already cancelled");
        }

        count.setStatus(InventoryCountStatus.CANCELLED);
        count.setCancelledDate(LocalDateTime.now());
        count.setCancelledBy(currentUserService.getCurrentUser());
        InventoryCount savedCount = inventoryCountRepository.save(count);
        return inventoryCountMapper.toInventoryCountResponse(savedCount);
    }

    @Transactional
    @Override
    public List<StockAdjustmentResponse> createAdjustment(UUID countId) {

        InventoryCount count = inventoryCountRepository.findById(countId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory count not found"));

        if (count.getStatus() != InventoryCountStatus.COMPLETED) {

            throw new BusinessException(
                    "Only completed inventory counts can create adjustment");
        }

        return stockAdjustmentService.createFromInventoryCount(countId);
    }

    private String generateInventoryCount() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long count = inventoryCountRepository.count() + 1;

        return String.format("IC-%s-%04d", date, count);
    }

    private void createFullCountItems(InventoryCount count) {

        List<WarehouseStock> stocks = warehouseStockRepository.findByWarehouseId(
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
