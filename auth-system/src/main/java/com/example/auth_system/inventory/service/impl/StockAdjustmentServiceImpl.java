package com.example.auth_system.inventory.service.impl;

import com.example.auth_system.product.mapper.ProductVariantMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.inventory.dto.request.stockAdjustment.CreateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.entity.StockAdjustment;
import com.example.auth_system.inventory.entity.StockMovement;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.enums.AdjustmentStatus;
import com.example.auth_system.inventory.enums.MovementType;
import com.example.auth_system.inventory.mapper.StockAdjustmentMapper;
import com.example.auth_system.inventory.repository.StockAdjustmentRepository;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.StockAdjustmentService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.product.repository.ProductRepository;
import com.example.auth_system.product.repository.ProductVariantRepository;
import com.example.auth_system.store.entity.Store;
import com.example.auth_system.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockAdjustmentServiceImpl implements StockAdjustmentService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final StoreRepository storeRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final StockAdjustmentRepository stockAdjustmentRepository;
    private final StockAdjustmentMapper stockAdjustmentMapper;

    @Override
    public StockAdjustmentResponse createStockAdjustment(CreateStockAdjustmentRequest request) {

        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Product not found"));

        ProductVariant variant = null;

        if (request.getVariantId() != null) {

            variant = productVariantRepository
                    .findById(request.getVariantId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Variant not found"));
        }

        Store warehouseStore = null;

        if (request.getWarehouseId() != null) {

            warehouseStore = storeRepository
                    .findById(request.getWarehouseId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Variant not found"));
        }

        // oldquantity = 100 ;
        // newquantity = 90;
        // difference = 90 - 100 = -10 (difference )

        WarehouseStock stock = warehouseStockRepository
                .findByProductIdAndVariantIdAndWarehouseId(
                        product.getId(),
                        variant.getId(),
                        warehouseStore.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Warehouse stock not found"));

        Integer oldQuantity = stock.getCurrentQuantity();
        Integer difference = request.getNewQuantity() - oldQuantity;

        StockAdjustment adjustment = StockAdjustment.builder()
                .adjustmentNumber(generateAdjustmentNumber())
                .product(product)
                .variant(variant)
                .warehouse(warehouseStore)
                .adjustmentType(request.getAdjustmentType())
                .direction(request.getDirection())
                .oldQuantity(oldQuantity)
                .newQuantity(request.getNewQuantity())
                .difference(difference)
                .reason(request.getReason())
                .status(AdjustmentStatus.PENDING)
                .build();
        StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);

        return stockAdjustmentMapper.toResponse(savedAdjustment);
    }

    private String generateAdjustmentNumber() {
        return "AJ-" + LocalDate.now()
                + "-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }

}
