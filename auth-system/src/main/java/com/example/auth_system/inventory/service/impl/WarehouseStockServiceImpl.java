package com.example.auth_system.inventory.service.impl;

import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.inventory.dto.response.LowStockResponse;
import com.example.auth_system.inventory.dto.response.StockSummaryResponse;
import com.example.auth_system.inventory.dto.response.WarehouseStockResponse;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.mapper.WarehouseStockMapper;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.WarehouseStockService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.product.repository.ProductRepository;
import com.example.auth_system.product.repository.ProductVariantRepository;
import com.example.auth_system.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WarehouseStockServiceImpl implements WarehouseStockService {

    private final WarehouseStockRepository warehouseStockRepository;
    private final WarehouseStockMapper warehouseStockMapper;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final StoreRepository storeRepository;

    @Override
    public List<WarehouseStockResponse> getAllWarehouseStocks() {

        List<WarehouseStock> warehouseStocks = warehouseStockRepository.findAll();
        return warehouseStocks.stream()
                .map(warehouseStockMapper::toWarehouseStockResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseStockResponse getWarehouseStockById(UUID warehouseStockId) {

        WarehouseStock warehouseStock = warehouseStockRepository.findById(warehouseStockId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        return warehouseStockMapper.toWarehouseStockResponse(warehouseStock);
    }

    @Override
    public List<WarehouseStockResponse> getStockByWarehouse(UUID storeId) {

        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + storeId));

        return warehouseStockRepository.findByWarehouseId(storeId)
                .stream()
                .map(warehouseStockMapper::toWarehouseStockResponse)
                .toList();
    }

    @Override
    public List<WarehouseStockResponse> getStockByProduct(UUID productId, UUID variantId) {

        productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "Product not found with id: " + productId));

        List<WarehouseStock> stocks;

        if (variantId != null) {
            stocks = warehouseStockRepository.findByProductIdAndVariantId(productId, variantId);
        } else {
            stocks = warehouseStockRepository.findByProductId(productId);
        }

        return stocks.stream()
                .map(warehouseStockMapper::toWarehouseStockResponse)
                .toList();
    }

    @Override
    public StockSummaryResponse getStockSummary(UUID productId, UUID variantId) {

        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        List<WarehouseStock> stocks;

        if (variantId != null) {
            stocks = warehouseStockRepository.findByProductIdAndVariantId(productId, variantId);
        } else {
            stocks = warehouseStockRepository.findByProductId(productId);
        }

        if (stocks.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found for product with id: " + productId);
        }

        WarehouseStock firstStock = stocks.get(0);

        Product product = firstStock.getProduct();

        ProductVariant variant = firstStock.getVariant();

        return warehouseStockMapper.toStockSummaryResponse(product, variant, stocks);
    }

    @Override
    public List<LowStockResponse> getLowStockProducts() {

        List<WarehouseStock> stocks = warehouseStockRepository.findLowStockItems();

        return stocks.stream()
                .map(warehouseStockMapper::tLowStockResponse)
                .toList();
    }

    @Override
    public List<WarehouseStockResponse> getOutOfStockProducts() {

        List<WarehouseStock> stocks = warehouseStockRepository.findOutOfStockItems();

        return stocks.stream()
                .map(warehouseStockMapper::toWarehouseStockResponse)
                .toList();
    }

    @Override
    public List<WarehouseStockResponse> getOverStockProducts() {

        List<WarehouseStock> stocks = warehouseStockRepository.findOverStockItems();

        return stocks.stream()
                .map(warehouseStockMapper::toWarehouseStockResponse)
                .toList();
    }

    @Override
    public void increaseStock(UUID warehouseId, UUID productId, UUID variantId, Integer quantity) {

        WarehouseStock stock = warehouseStockRepository.findByProductIdAndVariantIdAndWarehouseId(
                productId, variantId, warehouseId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Warehouse stock not found"));

        stock.increaseQuantity(quantity);
        stock.setLastUpdatedAt(LocalDateTime.now());
        warehouseStockRepository.save(stock);

    }

    @Override
    public void decreaseStock(UUID warehouseId, UUID productId, UUID variantId, Integer quantity) {
        WarehouseStock stock = warehouseStockRepository.findByProductIdAndVariantIdAndWarehouseId(
                productId, variantId, warehouseId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Warehouse stock not found"));

        stock.decreaseQuantity(quantity);
        stock.setLastUpdatedAt(LocalDateTime.now());
        warehouseStockRepository.save(stock);
    }

    @Override
    public void adjustStock(UUID warehouseId, UUID productId, UUID variantId, Integer difference) {
        WarehouseStock stock = warehouseStockRepository.findByProductIdAndVariantIdAndWarehouseId(
                productId, variantId, warehouseId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Warehouse stock not found"));

        if (difference > 0) {
            stock.increaseQuantity(difference);
        } else if (difference < 0) {
            stock.decreaseQuantity(Math.abs(difference));
        }

        stock.setLastUpdatedAt(LocalDateTime.now());
        warehouseStockRepository.save(stock);
    }

}