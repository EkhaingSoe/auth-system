package com.example.auth_system.inventory.service.impl;

import com.example.auth_system.inventory.repository.InventoryCountItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.inventory.dto.request.inventoryCount.CreateInventoryCountItemRequest;
import com.example.auth_system.inventory.dto.request.inventoryCount.UpdateCountedQuantityRequest;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountItemResponse;
import com.example.auth_system.inventory.dto.response.inventoryCount.InventoryCountResponse;
import com.example.auth_system.inventory.entity.InventoryCount;
import com.example.auth_system.inventory.entity.InventoryCountItem;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.enums.InventoryCountStatus;
import com.example.auth_system.inventory.mapper.InventoryCountItemMapper;
import com.example.auth_system.inventory.repository.InventoryCountRepository;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.InventoryCountItemService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.product.repository.ProductRepository;
import com.example.auth_system.product.repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryCountItemServiceImpl implements InventoryCountItemService {

        private final InventoryCountItemRepository inventoryCountItemRepository;
        private final InventoryCountRepository inventoryCountRepository;
        private final ProductRepository productRepository;
        private final ProductVariantRepository productVariantRepository;
        private final WarehouseStockRepository warehouseStockRepository;
        private final InventoryCountItemMapper inventoryCountItemMapper;

        @Transactional
        @Override
        public List<InventoryCountItemResponse> createInventoryCountitem(UUID inventoryCountId,
                        List<CreateInventoryCountItemRequest> requests) {

                InventoryCount count = inventoryCountRepository.findById(inventoryCountId).orElseThrow(
                                () -> new ResourceNotFoundException("Warehouse not found"));

                if (count.getStatus() != InventoryCountStatus.PENDING) {
                        throw new BusinessException(
                                        "Cannot add items after count started");
                }

                List<InventoryCountItem> items = new ArrayList<>();

                for (CreateInventoryCountItemRequest request : requests) {

                        Product product = productRepository.findById(request.getProductId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                        ProductVariant variant = null;

                        if (request.getVariantId() != null) {
                                variant = productVariantRepository.findById(request.getVariantId())
                                                .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
                        }

                        Integer systemQuantity = getCurrentStock(
                                        count.getWarehouse().getId(),
                                        product.getId(),
                                        request.getVariantId());

                        InventoryCountItem item = InventoryCountItem.builder()
                                        .inventoryCount(count)
                                        .product(product)
                                        .variant(variant)
                                        .systemQuantity(systemQuantity)
                                        .countedQuantity(0)
                                        .difference(0)
                                        .build();

                        items.add(item);
                }

                List<InventoryCountItem> savedItems = inventoryCountItemRepository.saveAll(items);

                return savedItems.stream()
                                .map(inventoryCountItemMapper::toResponse)
                                .toList();

        }

        @Override
        @Transactional(readOnly = true)
        public List<InventoryCountItemResponse> getByInventoryCount(UUID inventoryCountId) {

                List<InventoryCountItem> items = inventoryCountItemRepository.findByInventoryCountId(inventoryCountId);
                return items.stream()
                                .map(inventoryCountItemMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<InventoryCountItemResponse> getDiscrepancyItems(UUID inventoryCountId) {

                InventoryCount count = inventoryCountRepository.findById(inventoryCountId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Inventory count not found"));

                boolean isAllCounted = count.getItems()
                                .stream()
                                .allMatch(item -> item.getCountedQuantity() != null);

                if (!isAllCounted) {
                        throw new BusinessException(
                                        "Discrepancy items can only be viewed after all items have been counted");
                }

                List<InventoryCountItem> items = inventoryCountItemRepository
                                .findDiscrepancyItemsByCountId(inventoryCountId);
                return items.stream()
                                .map(inventoryCountItemMapper::toResponse)
                                .toList();
        }

        @Transactional
        @Override
        public InventoryCountItemResponse countedQuantity(UUID itemId, UpdateCountedQuantityRequest countedQuantity) {

                InventoryCountItem countItem = inventoryCountItemRepository.findById(itemId)
                                .orElseThrow(() -> new ResourceNotFoundException("Count Item not found"));

                InventoryCount inventoryCount = countItem.getInventoryCount();

                if (inventoryCount.getStatus() != InventoryCountStatus.IN_PROGRESS) {
                        throw new BusinessException(
                                        "Inventory count must be in progress");
                }

                countItem.setCountedQuantity(countedQuantity.getCountedQuantity());
                countItem.setDifference(countItem.getCountedQuantity() - countItem.getSystemQuantity());
                InventoryCountItem savedItem = inventoryCountItemRepository.save(countItem);

                return inventoryCountItemMapper.toResponse(savedItem);

        }

        private Integer getCurrentStock(UUID warehouseId, UUID productId, UUID variantId) {

                if (variantId != null) {
                        return warehouseStockRepository
                                        .findByProductIdAndVariantIdAndWarehouseId(productId, variantId, warehouseId)
                                        .map(WarehouseStock::getAvailableQuantity)
                                        .orElse(0);
                }

                return warehouseStockRepository
                                .findByProductIdAndVariantIdAndWarehouseId(
                                                productId,
                                                null,
                                                warehouseId)
                                .map(WarehouseStock::getAvailableQuantity)
                                .orElse(0);
        }
}
