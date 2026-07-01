package com.example.auth_system.customer.service.impl;

import com.example.auth_system.common.exception.BusinessException;
import com.example.auth_system.common.exception.ResourceNotFoundException;
import com.example.auth_system.customer.dto.request.AddWishlistItemRequest;
import com.example.auth_system.customer.dto.request.CreateWishlistRequest;
import com.example.auth_system.customer.dto.request.UpdateWishlistRequest;
import com.example.auth_system.customer.dto.response.WishlistResponse;
import com.example.auth_system.customer.entity.Customer;
import com.example.auth_system.customer.entity.Wishlist;
import com.example.auth_system.customer.entity.WishlistItem;
import com.example.auth_system.customer.mapper.WishlistMapper;
import com.example.auth_system.customer.repository.CustomerRepository;
import com.example.auth_system.customer.repository.WishlistItemRepository;
import com.example.auth_system.customer.repository.WishlistRepository;
import com.example.auth_system.customer.service.WishlistService;
import com.example.auth_system.product.entity.Product;
import com.example.auth_system.product.entity.ProductVariant;
import com.example.auth_system.product.repository.ProductRepository;
import com.example.auth_system.product.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final WishlistMapper wishlistMapper;

    // ============================================================
    // WISHLIST (PARENT) OPERATIONS
    // ============================================================

    @Override
    public WishlistResponse createWishlist(CreateWishlistRequest request) {
        log.info("Creating wishlist for customer: {}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Wishlist wishlist = wishlistMapper.toEntity(customer, request);
        wishlist = wishlistRepository.save(wishlist);

        log.info("Wishlist created successfully: {}", wishlist.getId());
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    public WishlistResponse updateWishlist(UUID wishlistId, UpdateWishlistRequest request) {
        log.info("Updating wishlist: {}", wishlistId);

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + wishlistId));

        if (request.getName() != null) {
            wishlist.setName(request.getName());
        }

        wishlist = wishlistRepository.save(wishlist);
        log.info("Wishlist updated successfully: {}", wishlistId);
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    @Transactional(readOnly = true)
    public WishlistResponse getWishlistById(UUID wishlistId) {
        log.info("Getting wishlist by id: {}", wishlistId);
        Wishlist wishlist = wishlistRepository.findWithItemsByIdAndCustomerId(wishlistId, null)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + wishlistId));
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    @Transactional(readOnly = true)
    public WishlistResponse getWishlistByIdAndCustomer(UUID wishlistId, UUID customerId) {
        log.info("Getting wishlist: {} for customer: {}", wishlistId, customerId);
        Wishlist wishlist = wishlistRepository.findWithItemsByIdAndCustomerId(wishlistId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for this customer"));
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishlistResponse> getWishlistsByCustomerId(UUID customerId) {
        log.info("Getting wishlists for customer: {}", customerId);
        return wishlistRepository.findWithItemsByCustomerId(customerId).stream()
                .map(wishlistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWishlist(UUID wishlistId) {
        log.info("Deleting wishlist: {}", wishlistId);
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + wishlistId));
        wishlistRepository.delete(wishlist);
        log.info("Wishlist deleted successfully: {}", wishlistId);
    }

    @Override
    public void deleteWishlistByIdAndCustomer(UUID wishlistId, UUID customerId) {
        log.info("Deleting wishlist: {} for customer: {}", wishlistId, customerId);
        Wishlist wishlist = wishlistRepository.findByIdAndCustomerId(wishlistId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for this customer"));
        wishlistRepository.delete(wishlist);
        log.info("Wishlist deleted successfully: {}", wishlistId);
    }

    // ============================================================
    // WISHLIST ITEM OPERATIONS
    // ============================================================

    @Override
    public WishlistResponse addItemToWishlist(AddWishlistItemRequest request) {
        log.info("Adding item to wishlist: {}", request.getWishlistId());

        // Find wishlist
        Wishlist wishlist = wishlistRepository.findById(request.getWishlistId())
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + request.getWishlistId()));

        // Find product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // Find variant if provided
        ProductVariant variant = null;
        if (request.getVariantId() != null) {
            variant = variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + request.getVariantId()));
            // Verify variant belongs to product
            if (!variant.getProduct().getId().equals(request.getProductId())) {
                throw new BusinessException("Variant does not belong to this product");
            }
        }

        // Check if already exists
        if (wishlistItemRepository.existsByWishlistIdAndProductIdAndVariantId(
                request.getWishlistId(), request.getProductId(), request.getVariantId())) {
            throw new BusinessException("Item already exists in wishlist");
        }

        // Create wishlist item
        WishlistItem item = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .variant(variant)
                .notes(request.getNotes())
                .build();

        wishlistItemRepository.save(item);
        wishlist.addItem(item);

        log.info("Item added to wishlist successfully");
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    public void removeItemFromWishlist(UUID itemId) {
        log.info("Removing item from wishlist: {}", itemId);
        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found with id: " + itemId));
        wishlistItemRepository.delete(item);
        log.info("Item removed from wishlist successfully");
    }

    @Override
    public void removeItemFromWishlist(UUID wishlistId, UUID productId) {
        log.info("Removing product: {} from wishlist: {}", productId, wishlistId);
        wishlistItemRepository.deleteByWishlistIdAndProductId(wishlistId, productId);
        log.info("Product removed from wishlist successfully");
    }

    @Override
    public void clearWishlist(UUID wishlistId) {
        log.info("Clearing wishlist: {}", wishlistId);
        List<WishlistItem> items = wishlistItemRepository.findByWishlistId(wishlistId);
        wishlistItemRepository.deleteAll(items);
        log.info("Wishlist cleared successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductInWishlist(UUID wishlistId, UUID productId, UUID variantId) {
        return wishlistItemRepository.existsByWishlistIdAndProductIdAndVariantId(wishlistId, productId, variantId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getWishlistItemCount(UUID wishlistId) {
        return (int) wishlistItemRepository.countByWishlistId(wishlistId);
    }
}