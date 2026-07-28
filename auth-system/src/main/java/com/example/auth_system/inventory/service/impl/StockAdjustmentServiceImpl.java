package com.example.auth_system.inventory.service.impl;

import com.example.auth_system.product.mapper.ProductVariantMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_system.auth.entity.User;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.common.service.CurrentUserService;
import com.example.auth_system.inventory.dto.request.stockAdjustment.ApproveStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.CreateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.request.stockAdjustment.UpdateStockAdjustmentRequest;
import com.example.auth_system.inventory.dto.response.StockAdjustmentResponse;
import com.example.auth_system.inventory.dto.response.StockMovementResponse;
import com.example.auth_system.inventory.entity.StockAdjustment;
import com.example.auth_system.inventory.entity.StockMovement;
import com.example.auth_system.inventory.entity.WarehouseStock;
import com.example.auth_system.inventory.enums.AdjustmentDirection;
import com.example.auth_system.inventory.enums.AdjustmentStatus;
import com.example.auth_system.inventory.enums.AdjustmentType;
import com.example.auth_system.inventory.enums.MovementType;
import com.example.auth_system.inventory.enums.ReferenceType;
import com.example.auth_system.inventory.mapper.StockAdjustmentMapper;
import com.example.auth_system.inventory.repository.StockAdjustmentRepository;
import com.example.auth_system.inventory.repository.WarehouseStockRepository;
import com.example.auth_system.inventory.service.StockAdjustmentService;
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
public class StockAdjustmentServiceImpl implements StockAdjustmentService {

        private final ProductRepository productRepository;
        private final ProductVariantRepository productVariantRepository;
        private final StoreRepository storeRepository;
        private final WarehouseStockRepository warehouseStockRepository;
        private final StockAdjustmentRepository stockAdjustmentRepository;
        private final StockAdjustmentMapper stockAdjustmentMapper;
        private final CurrentUserService currentUserService;
        private final StockMovementService stockMovementService;

        @Override
        @Transactional
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

        @Override
        public StockAdjustmentResponse updateStockAdjustment(UUID adjustmentId,
                        UpdateStockAdjustmentRequest request) {

                StockAdjustment adjustment = stockAdjustmentRepository.findById(adjustmentId).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Stock adjustment not found"));

                if (adjustment.getStatus() != AdjustmentStatus.PENDING) {

                        throw new RuntimeException(
                                        "Only pending adjustment can be updated");
                }

                if (request.getAdjustmentType() != null) {
                        adjustment.setAdjustmentType(request.getAdjustmentType());
                }

                if (request.getDirection() != null) {
                        adjustment.setDirection(request.getDirection());
                }

                if (request.getNewQuantity() != null) {
                        adjustment.setNewQuantity(request.getNewQuantity());
                        adjustment.setDifference(request.getNewQuantity() - adjustment.getOldQuantity());
                }

                if (request.getReason() != null) {
                        adjustment.setReason(request.getReason());
                }

                StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);

                return stockAdjustmentMapper.toResponse(savedAdjustment);
        }

        @Override
        public StockAdjustmentResponse getStockAdjustmentById(UUID adjustmentId) {

                StockAdjustment stockAdjustment = stockAdjustmentRepository.findById(adjustmentId)
                                .orElseThrow(() -> new ResourceNotFoundException("StockAdjustment not found"));
                return stockAdjustmentMapper.toResponse(stockAdjustment);
        }

        @Override
        public StockAdjustmentResponse getStockAdjustmentByNumber(String adjustmentNumber) {

                StockAdjustment stockAdjustment = stockAdjustmentRepository.findByAdjustmentNumber(adjustmentNumber)
                                .orElseThrow(() -> new ResourceNotFoundException("StockAdjustment not found"));
                return stockAdjustmentMapper.toResponse(stockAdjustment);
        }

        @Override
        public List<StockAdjustmentResponse> getAllStockAdjustments() {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findAll();
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StockAdjustmentResponse> getStockAdjustmentsByStatus(AdjustmentStatus status) {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findByStatus(status);
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StockAdjustmentResponse> getPendingStockAdjustments() {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findPendingAdjustments();
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StockAdjustmentResponse> getStockAdjustmentsByProduct(UUID productId) {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findByProductId(productId);
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StockAdjustmentResponse> getStockAdjustmentsByWarehouse(UUID warehouseId) {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findByWarehouseId(warehouseId);
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StockAdjustmentResponse> getStockAdjustmentsByType(AdjustmentType adjustmentType) {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findByAdjustmentType(adjustmentType);
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StockAdjustmentResponse> getStockAdjustmentsByDateRange(LocalDateTime start, LocalDateTime end) {

                List<StockAdjustment> stockAdjustments = stockAdjustmentRepository.findByCreatedAtBetween(start, end);
                return stockAdjustments.stream()
                                .map(stockAdjustmentMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public StockAdjustmentResponse approveStockAdjustment(UUID adjustmentId,
                        ApproveStockAdjustmentRequest request) {

                StockAdjustment adjustment = stockAdjustmentRepository.findById(adjustmentId).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Stock adjustment not found"));

                if (adjustment.getStatus() != AdjustmentStatus.PENDING) {

                        throw new RuntimeException(
                                        "Only pending adjustment can be approved");
                }

                User user = currentUserService.getCurrentUser();
                adjustment.setStatus(AdjustmentStatus.APPROVED);
                adjustment.setApprovedBy(user);
                adjustment.setApprovedAt(LocalDateTime.now());

                StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);

                return stockAdjustmentMapper.toResponse(savedAdjustment);
        }

        @Override
        public StockAdjustmentResponse rejectStockAdjustment(UUID adjustmentId, String reason) {

                StockAdjustment adjustment = stockAdjustmentRepository.findById(adjustmentId).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Stock adjustment not found"));

                if (adjustment.getStatus() != AdjustmentStatus.PENDING) {

                        throw new RuntimeException(
                                        "Only pending adjustment can be rejected");
                }

                User user = currentUserService.getCurrentUser();
                adjustment.setStatus(AdjustmentStatus.REJECTED);
                adjustment.setApprovedBy(user);
                adjustment.setApprovedAt(LocalDateTime.now());
                adjustment.setReason(reason);

                StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);

                return stockAdjustmentMapper.toResponse(savedAdjustment);
        }

        @Override
        @Transactional
        public StockAdjustmentResponse completeStockAdjustment(UUID adjustmentId) {

                StockAdjustment adjustment = stockAdjustmentRepository.findById(adjustmentId).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Stock adjustment not found"));

                if (adjustment.getStatus() != AdjustmentStatus.APPROVED) {

                        throw new RuntimeException(
                                        "Only approved adjustment can be completed");
                }

                WarehouseStock stock = warehouseStockRepository
                                .findByProductIdAndVariantIdAndWarehouseId(
                                                adjustment.getProduct().getId(),
                                                adjustment.getVariant() != null
                                                                ? adjustment.getVariant().getId()
                                                                : null,
                                                adjustment.getWarehouse().getId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Warehouse stock not found"));

                int difference = Math.abs(adjustment.getDifference());

                if (adjustment.getDirection() == AdjustmentDirection.INCREASE) {
                        stock.increaseQuantity(difference);
                } else {
                        if (adjustment.getDirection() == AdjustmentDirection.DECREASE
                                        && stock.getAvailableQuantity() < Math.abs(adjustment.getDifference())) {

                                throw new RuntimeException("Insufficient stock");
                        }
                        stock.decreaseQuantity(difference);
                }

                warehouseStockRepository.save(stock);
                User user = currentUserService.getCurrentUser();
                MovementType movementType = getMovementType(adjustment.getDirection());

                StockMovement movement = stockMovementService.createMovement(
                                movementType,
                                adjustment.getProduct(),
                                adjustment.getVariant(),
                                adjustment.getWarehouse(), // fromWarehouse
                                null, // toWarehouse
                                Math.abs(adjustment.getDifference()), // quantity
                                adjustment.getOldQuantity(), // previousQuantity
                                adjustment.getNewQuantity(), // newQuantity
                                null,
                                adjustment.getId(),
                                ReferenceType.STOCK_ADJUSTMENT,
                                adjustment.getReason(),
                                user);

                adjustment.setStockMovement(movement);
                adjustment.setStatus(AdjustmentStatus.COMPLETED);
                adjustment.setCompletedAt(LocalDateTime.now());
                adjustment.setCompletedBy(user);
                StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);

                return stockAdjustmentMapper.toResponse(savedAdjustment);
        }

        private String generateAdjustmentNumber() {
                return "AJ-" + LocalDate.now()
                                + "-" + UUID.randomUUID()
                                                .toString()
                                                .substring(0, 8);
        }

        private MovementType getMovementType(AdjustmentDirection direction) {
                return direction == AdjustmentDirection.INCREASE
                                ? MovementType.ADJUSTMENT_IN
                                : MovementType.ADJUSTMENT_OUT;
        }

}
