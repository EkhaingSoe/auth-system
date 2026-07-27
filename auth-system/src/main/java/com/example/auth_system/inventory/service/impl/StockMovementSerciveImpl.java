package com.example.auth_system.inventory.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.inventory.dto.request.stockMovement.TransferStockRequest;
import com.example.auth_system.inventory.dto.response.StockInOutResponse;
import com.example.auth_system.inventory.dto.response.StockMovementResponse;
import com.example.auth_system.inventory.dto.response.StockMovementSummaryResponse;
import com.example.auth_system.inventory.dto.response.WarehouseStockResponse;
import com.example.auth_system.inventory.entity.StockMovement;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.enums.MovementType;
import com.example.auth_system.inventory.enums.ReferenceType;
import com.example.auth_system.inventory.mapper.StockMovementMapper;
import com.example.auth_system.inventory.repository.StockMovementRepository;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.StockMovementService;
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
public class StockMovementSerciveImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final StoreRepository storeRepository;
    private final WarehouseStockRepository warehouseStockRepository;

    @Override
    public List<StockMovementResponse> getAllStockMovements() {

        List<StockMovement> stockMovements = stockMovementRepository.findAll();
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StockMovementResponse getStockMovementById(UUID movementId) {

        StockMovement stockMovement = stockMovementRepository.findById(movementId)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement not found"));
        return stockMovementMapper.toResponse(stockMovement);
    }

    @Override
    public StockMovementResponse getStockMovementByNumber(String movementNumber) {

        StockMovement stockMovement = stockMovementRepository.findByMovementNumber(movementNumber)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement not found"));
        return stockMovementMapper.toResponse(stockMovement);
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByProduct(UUID productId) {

        List<StockMovement> stockMovements = stockMovementRepository.findByProductId(productId);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByVariant(UUID variantId) {

        List<StockMovement> stockMovements = stockMovementRepository.findByVariantId(variantId);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByWarehouse(UUID warehouseId) {

        List<StockMovement> stockMovements = stockMovementRepository.findByWarehouseId(warehouseId);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByType(MovementType movementType) {

        List<StockMovement> stockMovements = stockMovementRepository.findByMovementType(movementType);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByDateRange(LocalDateTime start, LocalDateTime end) {

        List<StockMovement> stockMovements = stockMovementRepository.findByDateRange(start, end);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByReference(UUID referenceId) {

        List<StockMovement> stockMovements = stockMovementRepository.findByReferenceId(referenceId);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StockMovementResponse transferStock(TransferStockRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductVariant productVariant = null;
        Store fromStore = null;
        Store toStore = null;

        if (request.getVariantId() != null) {
            productVariant = productVariantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found"));
        }

        if (request.getFromWarehouseId() != null) {
            fromStore = storeRepository.findById(request.getFromWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source Warehouse not found"));
        }

        if (request.getToWarehouseId() != null) {
            toStore = storeRepository.findById(request.getToWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination Warehouse not found"));
        }

        WarehouseStock fromStock = warehouseStockRepository.findByProductIdAndVariantIdAndWarehouseId(
                product.getId(), productVariant.getId(), fromStore.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Source warehouse stock not found"));

        WarehouseStock toStock = warehouseStockRepository.findByProductIdAndVariantIdAndWarehouseId(
                product.getId(),
                productVariant.getId(),
                toStore.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination warehouse stock not found"));

        Integer availableQuantity = fromStock.getAvailableQuantity();

        if (request.getQuantity() > availableQuantity) {

            throw new RuntimeException(
                    "Insufficient stock available");
        }
        fromStock.decreaseQuantity(request.getQuantity());
        toStock.increaseQuantity(request.getQuantity());
        warehouseStockRepository.save(fromStock);
        warehouseStockRepository.save(toStock);

        StockMovement transferOut = StockMovement.builder()
                .product(product)
                .variant(productVariant)
                .fromWarehouse(fromStore)
                .quantity(request.getQuantity())
                .movementType(MovementType.TRANSFER_OUT)
                .build();

        // Movement IN
        StockMovement transferIn = StockMovement.builder()
                .product(product)
                .variant(productVariant)
                .toWarehouse(toStore)
                .quantity(request.getQuantity())
                .movementType(MovementType.TRANSFER_IN)
                .build();

        stockMovementRepository.save(transferOut);
        StockMovement saved = stockMovementRepository.save(transferIn);

        return stockMovementMapper.toResponse(saved);
    }

    @Override
    public StockMovementSummaryResponse getStockMovementSummary(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<StockMovement> movementsByProduct = stockMovementRepository.findByProductId(productId);

        if (movementsByProduct.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No stock movement found");
        }

        int purchaseQuantity = 0;
        int saleQuantity = 0;
        int transferInQuantity = 0;
        int transferOutQuantity = 0;
        int adjustmentInQuantity = 0;
        int adjustmentOutQuantity = 0;

        for (StockMovement movement : movementsByProduct) {

            switch (movement.getMovementType()) {

                case PURCHASE_IN:
                    purchaseQuantity += movement.getQuantity();
                    break;

                case SALES_OUT:
                    saleQuantity += movement.getQuantity();
                    break;

                case TRANSFER_IN:
                    transferInQuantity += movement.getQuantity();
                    break;

                case TRANSFER_OUT:
                    transferOutQuantity += movement.getQuantity();
                    break;

                case ADJUSTMENT_IN:
                    adjustmentInQuantity += movement.getQuantity();
                    break;

                case ADJUSTMENT_OUT:
                    adjustmentOutQuantity += Math.abs(movement.getQuantity());
                    break;
            }
        }
        int totalIncoming = purchaseQuantity + transferInQuantity + adjustmentInQuantity;

        int totalOutgoing = saleQuantity + transferOutQuantity + adjustmentOutQuantity;

        return StockMovementSummaryResponse.builder()

                .productId(product.getId())
                .productName(product.getName())
                .purchaseQuantity(purchaseQuantity)
                .saleQuantity(saleQuantity)
                .transferInQuantity(transferInQuantity)
                .transferOutQuantity(transferOutQuantity)
                .adjustmentInQuantity(adjustmentInQuantity)
                .adjustmentOutQuantity(adjustmentOutQuantity)
                .totalIncoming(totalIncoming)
                .totalOutgoing(totalOutgoing)
                .build();
    }

    @Override
    public StockInOutResponse getTotalInOutQuantity(UUID productId) {

        StockMovementSummaryResponse summary = getStockMovementSummary(productId);
        return StockInOutResponse.builder()
                .totalIn(summary.getTotalIncoming())
                .totalOut(summary.getTotalOutgoing())
                .build();
    }

    @Override
    public StockMovement createMovement(
            MovementType movementType,
            Product product,
            ProductVariant variant,
            Store fromWarehouse,
            Store toWarehouse,
            Integer quantity,
            Integer previousQuantity,
            Integer newQuantity,
            BigDecimal unitCost,
            UUID referenceId,
            ReferenceType referenceType,
            String notes,
            User createdBy) {

        StockMovement movement = StockMovement.builder()
                .movementNumber(generateMovementNumber())

                .product(product)
                .variant(variant)

                .fromWarehouse(fromWarehouse)
                .toWarehouse(toWarehouse)

                .movementType(movementType)

                .quantity(quantity)

                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)

                .unitCost(unitCost)

                .totalCost(
                        unitCost != null
                                ? unitCost.multiply(
                                        BigDecimal.valueOf(quantity))
                                : null)

                .referenceId(referenceId)
                .referenceType(referenceType)

                .notes(notes)

                .createdBy(createdBy)

                .build();

        return stockMovementRepository.save(movement);
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByProductAndDateRange(
            UUID productId,
            LocalDateTime start,
            LocalDateTime end) {

        List<StockMovement> stockMovements = stockMovementRepository.findByProductIdAndDateRange(productId, start, end);
        return stockMovements.stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String generateMovementNumber() {
        return "SM-" + LocalDate.now()
                + "-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }
}
