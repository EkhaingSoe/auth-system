
```
auth-system
├─ auth-system
│  ├─ .mvn
│  │  └─ wrapper
│  │     └─ maven-wrapper.properties
│  ├─ auth-system
│  │  ├─ .mvn
│  │  │  └─ wrapper
│  │  │     └─ maven-wrapper.properties
│  │  ├─ mvnw
│  │  ├─ mvnw.cmd
│  │  ├─ pom.xml
│  │  └─ src
│  │     ├─ main
│  │     │  ├─ java
│  │     │  │  └─ com
│  │     │  │     └─ example
│  │     │  │        └─ auth_system
│  │     │  │           └─ AuthSystemApplication.java
│  │     │  └─ resources
│  │     │     └─ application.properties
│  │     └─ test
│  │        └─ java
│  │           └─ com
│  │              └─ example
│  │                 └─ auth_system
│  │                    └─ AuthSystemApplicationTests.java
│  ├─ logs
│  │  ├─ auth-system.log
│  │  ├─ auth-system.log.2026-07-06.0.gz
│  │  ├─ auth-system.log.2026-07-07.0.gz
│  │  ├─ auth-system.log.2026-07-08.0.gz
│  │  ├─ auth-system.log.2026-07-09.0.gz
│  │  ├─ auth-system.log.2026-07-11.0.gz
│  │  ├─ auth-system.log.2026-07-13.0.gz
│  │  ├─ auth-system.log.2026-07-14.0.gz
│  │  ├─ auth-system.log.2026-07-15.0.gz
│  │  ├─ auth-system.log.2026-07-16.0.gz
│  │  ├─ auth-system.log.2026-07-17.0.gz
│  │  ├─ auth-system.log.2026-07-18.0.gz
│  │  ├─ auth-system.log.2026-07-20.0.gz
│  │  ├─ auth-system.log.2026-07-21.0.gz
│  │  ├─ auth-system.log.2026-07-22.0.gz
│  │  ├─ auth-system.log.2026-07-23.0.gz
│  │  ├─ auth-system.log.2026-07-24.0.gz
│  │  ├─ auth-system.log.2026-07-25.0.gz
│  │  ├─ auth-system.log.2026-07-27.0.gz
│  │  ├─ auth-system.log.2026-07-28.0.gz
│  │  ├─ auth-system.log.2026-07-30.0.gz
│  │  └─ auth-system.log.2026-07-31.0.gz
│  ├─ mvnw
│  ├─ mvnw.cmd
│  ├─ pom.xml
│  ├─ src
│  │  ├─ main
│  │  │  ├─ java
│  │  │  │  └─ com
│  │  │  │     └─ example
│  │  │  │        └─ auth_system
│  │  │  │           ├─ auth
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  ├─ AuthController.java
│  │  │  │           │  │  └─ PermissionController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ ChangePasswordRequest.java
│  │  │  │           │  │  │  ├─ ForgotPasswordRequest.java
│  │  │  │           │  │  │  ├─ LoginRequest.java
│  │  │  │           │  │  │  ├─ RefreshTokenRequest.java
│  │  │  │           │  │  │  ├─ RegisterRequest.java
│  │  │  │           │  │  │  ├─ ResetPasswordRequest.java
│  │  │  │           │  │  │  ├─ SendOtpRequest.java
│  │  │  │           │  │  │  └─ VerifyOtpRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     ├─ AuthResponse.java
│  │  │  │           │  │     ├─ LoginResponse.java
│  │  │  │           │  │     ├─ RefreshTokenResponse.java
│  │  │  │           │  │     └─ UserInfoResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  ├─ OtpToken.java
│  │  │  │           │  │  ├─ OtpType.java
│  │  │  │           │  │  ├─ PasswordResetToken.java
│  │  │  │           │  │  ├─ Permission.java
│  │  │  │           │  │  ├─ Role.java
│  │  │  │           │  │  ├─ RoleName.java
│  │  │  │           │  │  └─ User.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  ├─ OtpTokenRepository.java
│  │  │  │           │  │  ├─ PasswordResetTokenRepository.java
│  │  │  │           │  │  ├─ PermissionRepository.java
│  │  │  │           │  │  ├─ RolePermissionRepository.java
│  │  │  │           │  │  ├─ RoleRepository.java
│  │  │  │           │  │  └─ UserRepository.java
│  │  │  │           │  ├─ security
│  │  │  │           │  │  ├─ CustomUserDetails.java
│  │  │  │           │  │  ├─ CustomUserDetailsService.java
│  │  │  │           │  │  ├─ JwtAuthenticationFilter.java
│  │  │  │           │  │  ├─ JwtTokenProvider.java
│  │  │  │           │  │  └─ PermissionEvaluator.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ AuthService.java
│  │  │  │           │     ├─ AuthServiceImpl.java
│  │  │  │           │     ├─ OtpService.java
│  │  │  │           │     ├─ PermissionManagementService.java
│  │  │  │           │     ├─ PermissionManagementServiceImpl.java
│  │  │  │           │     ├─ PermissionService.java
│  │  │  │           │     └─ PermissionServiceImpl.java
│  │  │  │           ├─ AuthSystemApplication.java
│  │  │  │           ├─ brand
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  └─ BrandController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ CreateBrandRequest.java
│  │  │  │           │  │  │  └─ UpdateBrandRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     └─ BrandResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  └─ Brand.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  └─ BrandMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  └─ BrandRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ BrandService.java
│  │  │  │           │     └─ impl
│  │  │  │           │        └─ BrandServiceImpl.java
│  │  │  │           ├─ category
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  └─ CategoryController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ CategoryImageRequest.java
│  │  │  │           │  │  │  ├─ CreateCategoryRequest.java
│  │  │  │           │  │  │  └─ UpdateCategoryRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     └─ CategoryResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  ├─ Category.java
│  │  │  │           │  │  └─ CategoryImage.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  └─ CategoryMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  ├─ CategoryImageRepository.java
│  │  │  │           │  │  └─ CategoryRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ CategoryService.java
│  │  │  │           │     └─ impl
│  │  │  │           │        └─ CategoryServiceImpl.java
│  │  │  │           ├─ common
│  │  │  │           │  ├─ bootstrap
│  │  │  │           │  │  └─ AdminInitializer.java
│  │  │  │           │  ├─ config
│  │  │  │           │  │  ├─ CloudinaryConfig.java
│  │  │  │           │  │  ├─ RestTemplateConfig.java
│  │  │  │           │  │  ├─ SecurityConfig.java
│  │  │  │           │  │  ├─ SwaggerConfig.java
│  │  │  │           │  │  └─ WebConfig.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     ├─ ApiResponse.java
│  │  │  │           │  │     └─ PageResponse.java
│  │  │  │           │  ├─ exception
│  │  │  │           │  │  ├─ AuthException.java
│  │  │  │           │  │  ├─ BusinessException.java
│  │  │  │           │  │  ├─ ErrorResponse.java
│  │  │  │           │  │  ├─ GlobalExceptionHandler.java
│  │  │  │           │  │  ├─ InvalidCredentialsException.java
│  │  │  │           │  │  ├─ InvalidTokenException.java
│  │  │  │           │  │  ├─ OtpValidationException.java
│  │  │  │           │  │  ├─ ResourceNotFoundException.java
│  │  │  │           │  │  └─ UserAlreadyExistsException.java
│  │  │  │           │  ├─ service
│  │  │  │           │  │  ├─ CloudinaryService.java
│  │  │  │           │  │  ├─ CurrentUserService.java
│  │  │  │           │  │  └─ EmailService.java
│  │  │  │           │  └─ util
│  │  │  │           │     ├─ DateUtils.java
│  │  │  │           │     ├─ JwtUtil.java
│  │  │  │           │     ├─ OtpGenerator.java
│  │  │  │           │     └─ ValidationUtils.java
│  │  │  │           ├─ customer
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  ├─ CustomerController.java
│  │  │  │           │  │  ├─ CustomerGroupController.java
│  │  │  │           │  │  └─ WishlistController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ AddWishlistItemRequest.java
│  │  │  │           │  │  │  ├─ CreateCustomerGroupRequest.java
│  │  │  │           │  │  │  ├─ CreateCustomerRequest.java
│  │  │  │           │  │  │  ├─ CreateWishlistRequest.java
│  │  │  │           │  │  │  ├─ UpdateCustomerGroupRequest.java
│  │  │  │           │  │  │  ├─ UpdateCustomerRequest.java
│  │  │  │           │  │  │  └─ UpdateWishlistRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     ├─ CustomerGroupResponse.java
│  │  │  │           │  │     ├─ CustomerResponse.java
│  │  │  │           │  │     └─ WishlistResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  ├─ Customer.java
│  │  │  │           │  │  ├─ CustomerGroup.java
│  │  │  │           │  │  ├─ Wishlist.java
│  │  │  │           │  │  └─ WishlistItem.java
│  │  │  │           │  ├─ enums
│  │  │  │           │  │  └─ Gender.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  ├─ CustomerGroupMapper.java
│  │  │  │           │  │  ├─ CustomerMapper.java
│  │  │  │           │  │  └─ WishlistMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  ├─ CustomerGroupRepository.java
│  │  │  │           │  │  ├─ CustomerRepository.java
│  │  │  │           │  │  ├─ WishlistItemRepository.java
│  │  │  │           │  │  └─ WishlistRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ CustomerGroupService.java
│  │  │  │           │     ├─ CustomerService.java
│  │  │  │           │     ├─ impl
│  │  │  │           │     │  ├─ CustomerGroupServiceImpl.java
│  │  │  │           │     │  ├─ CustomerServiceImpl.java
│  │  │  │           │     │  └─ WishlistServiceImpl.java
│  │  │  │           │     └─ WishlistService.java
│  │  │  │           ├─ inventory
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  ├─ InventoryCountController.java
│  │  │  │           │  │  ├─ InventoryCountItemController.java
│  │  │  │           │  │  ├─ StockAdjustmentController.java
│  │  │  │           │  │  ├─ StockMovementController.java
│  │  │  │           │  │  └─ WarehouseStockController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ inventoryCount
│  │  │  │           │  │  │  │  ├─ CompleteInventoryCountRequest.java
│  │  │  │           │  │  │  │  ├─ CreateInventoryCountItemRequest.java
│  │  │  │           │  │  │  │  ├─ CreateInventoryCountRequest.java
│  │  │  │           │  │  │  │  ├─ UpdateCountedQuantityRequest.java
│  │  │  │           │  │  │  │  ├─ UpdateInventoryCountItemsRequest.java
│  │  │  │           │  │  │  │  ├─ UpdateInventoryCountRequest.java
│  │  │  │           │  │  │  │  └─ VerifyInventoryCountRequest.java
│  │  │  │           │  │  │  ├─ stockAdjustment
│  │  │  │           │  │  │  │  ├─ ApproveStockAdjustmentRequest.java
│  │  │  │           │  │  │  │  ├─ CreateStockAdjustmentRequest.java
│  │  │  │           │  │  │  │  └─ UpdateStockAdjustmentRequest.java
│  │  │  │           │  │  │  └─ stockMovement
│  │  │  │           │  │  │     ├─ CreateStockMovementRequest.java
│  │  │  │           │  │  │     └─ TransferStockRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     ├─ inventoryCount
│  │  │  │           │  │     │  ├─ InventoryCountDetailResponse.java
│  │  │  │           │  │     │  ├─ InventoryCountItemResponse.java
│  │  │  │           │  │     │  └─ InventoryCountResponse.java
│  │  │  │           │  │     ├─ InventoryDashboardResponse.java
│  │  │  │           │  │     ├─ LowStockResponse.java
│  │  │  │           │  │     ├─ StockAdjustmentResponse.java
│  │  │  │           │  │     ├─ StockAdjustmentSummaryResponse.java
│  │  │  │           │  │     ├─ StockInOutResponse.java
│  │  │  │           │  │     ├─ StockMovementResponse.java
│  │  │  │           │  │     ├─ StockMovementSummaryResponse.java
│  │  │  │           │  │     ├─ StockSummaryResponse.java
│  │  │  │           │  │     └─ WarehouseStockResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  ├─ InventoryCount.java
│  │  │  │           │  │  ├─ InventoryCountItem.java
│  │  │  │           │  │  ├─ StockAdjustment.java
│  │  │  │           │  │  ├─ StockMovement.java
│  │  │  │           │  │  └─ WarehouseStock.java
│  │  │  │           │  ├─ enums
│  │  │  │           │  │  ├─ AdjustmentDirection.java
│  │  │  │           │  │  ├─ AdjustmentStatus.java
│  │  │  │           │  │  ├─ AdjustmentType.java
│  │  │  │           │  │  ├─ CountType.java
│  │  │  │           │  │  ├─ InventoryCountStatus.java
│  │  │  │           │  │  ├─ MovementType.java
│  │  │  │           │  │  ├─ ReferenceType.java
│  │  │  │           │  │  └─ StockStatus.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  ├─ InventoryCountItemMapper.java
│  │  │  │           │  │  ├─ InventoryCountMapper.java
│  │  │  │           │  │  ├─ StockAdjustmentMapper.java
│  │  │  │           │  │  ├─ StockMovementMapper.java
│  │  │  │           │  │  └─ WarehouseStockMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  ├─ InventoryCountItemRepository.java
│  │  │  │           │  │  ├─ InventoryCountReportRepository.java
│  │  │  │           │  │  ├─ InventoryCountRepository.java
│  │  │  │           │  │  ├─ projection
│  │  │  │           │  │  │  └─ StockAdjustmentSummaryProjection.java
│  │  │  │           │  │  ├─ StockAdjustmentRepository.java
│  │  │  │           │  │  ├─ StockMovementRepository.java
│  │  │  │           │  │  └─ WarehouseStockRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ impl
│  │  │  │           │     │  ├─ InventoryCountItemServiceImpl.java
│  │  │  │           │     │  ├─ InventoryCountServiceImpl.java
│  │  │  │           │     │  ├─ StockAdjustmentServiceImpl.java
│  │  │  │           │     │  ├─ StockMovementSerciveImpl.java
│  │  │  │           │     │  └─ WarehouseStockServiceImpl.java
│  │  │  │           │     ├─ InventoryCountItemService.java
│  │  │  │           │     ├─ InventoryCountService.java
│  │  │  │           │     ├─ StockAdjustmentService.java
│  │  │  │           │     ├─ StockMovementService.java
│  │  │  │           │     └─ WarehouseStockService.java
│  │  │  │           ├─ order
│  │  │  │           │  ├─ order
│  │  │  │           │  │  ├─ controller
│  │  │  │           │  │  │  └─ OrderController.java
│  │  │  │           │  │  ├─ dto
│  │  │  │           │  │  │  ├─ orderRequest
│  │  │  │           │  │  │  │  ├─ CreateOrderItemRequest.java
│  │  │  │           │  │  │  │  ├─ CreateOrderRequest.java
│  │  │  │           │  │  │  │  ├─ UpdateOrderInternalNoteRequest.java
│  │  │  │           │  │  │  │  ├─ UpdateOrderPricingRequest.java
│  │  │  │           │  │  │  │  ├─ UpdateOrderRequest.java
│  │  │  │           │  │  │  │  └─ UpdateOrderStatusRequest.java
│  │  │  │           │  │  │  └─ orderResponse
│  │  │  │           │  │  │     ├─ AddressInfoResponse.java
│  │  │  │           │  │  │     ├─ CustomerInfoResponse.java
│  │  │  │           │  │  │     ├─ OrderItemResponse.java
│  │  │  │           │  │  │     ├─ OrderResponse.java
│  │  │  │           │  │  │     ├─ OrderSummaryResponse.java
│  │  │  │           │  │  │     ├─ OrderTimelineResponse.java
│  │  │  │           │  │  │     └─ UserInfoResponse.java
│  │  │  │           │  │  ├─ entity
│  │  │  │           │  │  │  ├─ Order.java
│  │  │  │           │  │  │  ├─ OrderItem.java
│  │  │  │           │  │  │  └─ OrderStatusHistory.java
│  │  │  │           │  │  ├─ enums
│  │  │  │           │  │  │  ├─ FulfillmentStatus.java
│  │  │  │           │  │  │  ├─ OrderStatus.java
│  │  │  │           │  │  │  └─ OrderType.java
│  │  │  │           │  │  ├─ mapper
│  │  │  │           │  │  │  ├─ OrderItemMapper.java
│  │  │  │           │  │  │  └─ OrderMapper.java
│  │  │  │           │  │  ├─ repository
│  │  │  │           │  │  │  ├─ OrderItemRepository.java
│  │  │  │           │  │  │  ├─ OrderRepository.java
│  │  │  │           │  │  │  ├─ OrderShipmentRepository.java
│  │  │  │           │  │  │  └─ OrderStatusHistoryRepository.java
│  │  │  │           │  │  └─ service
│  │  │  │           │  │     ├─ impl
│  │  │  │           │  │     │  ├─ OrderServiceImpl.java
│  │  │  │           │  │     │  └─ OrderStatusServiceImpl.java
│  │  │  │           │  │     ├─ OrderService.java
│  │  │  │           │  │     └─ OrderStatusService.java
│  │  │  │           │  ├─ payment
│  │  │  │           │  │  ├─ controller
│  │  │  │           │  │  │  └─ PaymentController.java
│  │  │  │           │  │  ├─ dto
│  │  │  │           │  │  │  ├─ paymentRequest
│  │  │  │           │  │  │  │  ├─ CreateEcommercePaymentRequest.java
│  │  │  │           │  │  │  │  ├─ CreatePaymentRequest.java
│  │  │  │           │  │  │  │  ├─ CreatePosPaymentRequest.java
│  │  │  │           │  │  │  │  └─ PaymentWebhookRequest.java
│  │  │  │           │  │  │  └─ paymentResponse
│  │  │  │           │  │  │     ├─ EcommercePaymentResponse.java
│  │  │  │           │  │  │     └─ PaymentResponse.java
│  │  │  │           │  │  ├─ entity
│  │  │  │           │  │  │  └─ Payment.java
│  │  │  │           │  │  ├─ enums
│  │  │  │           │  │  │  ├─ PaymentMethod.java
│  │  │  │           │  │  │  └─ PaymentStatus.java
│  │  │  │           │  │  ├─ mapper
│  │  │  │           │  │  │  └─ PaymentMapper.java
│  │  │  │           │  │  ├─ repository
│  │  │  │           │  │  │  └─ PaymentRepository.java
│  │  │  │           │  │  └─ service
│  │  │  │           │  │     ├─ impl
│  │  │  │           │  │     │  └─ PaymentServiceImpl.java
│  │  │  │           │  │     └─ PaymentService.java
│  │  │  │           │  ├─ refund
│  │  │  │           │  │  ├─ controller
│  │  │  │           │  │  ├─ dto
│  │  │  │           │  │  │  ├─ refundRequest
│  │  │  │           │  │  │  │  ├─ ProcessRefundRequest.java
│  │  │  │           │  │  │  │  └─ RefundItemRequest.java
│  │  │  │           │  │  │  └─ refundResponse
│  │  │  │           │  │  │     ├─ RefundItemResponse.java
│  │  │  │           │  │  │     └─ RefundResponse.java
│  │  │  │           │  │  ├─ entity
│  │  │  │           │  │  │  ├─ Notification.java
│  │  │  │           │  │  │  ├─ Refund.java
│  │  │  │           │  │  │  └─ RefundItem.java
│  │  │  │           │  │  ├─ enums
│  │  │  │           │  │  │  ├─ RefundMethod.java
│  │  │  │           │  │  │  ├─ RefundStatus.java
│  │  │  │           │  │  │  └─ RefundType.java
│  │  │  │           │  │  ├─ mapper
│  │  │  │           │  │  │  ├─ RefundItemMapper.java
│  │  │  │           │  │  │  └─ RefundMapper.java
│  │  │  │           │  │  ├─ repository
│  │  │  │           │  │  │  ├─ NotificationRepository.java
│  │  │  │           │  │  │  ├─ RefundItemRepository.java
│  │  │  │           │  │  │  └─ RefundRepository.java
│  │  │  │           │  │  └─ service
│  │  │  │           │  │     ├─ impl
│  │  │  │           │  │     │  └─ RefundServiceImpl.java
│  │  │  │           │  │     └─ RefundService.java
│  │  │  │           │  └─ shipment
│  │  │  │           │     ├─ controller
│  │  │  │           │     ├─ dto
│  │  │  │           │     │  ├─ shipmentRequest
│  │  │  │           │     │  │  ├─ BillingAddressRequest.java
│  │  │  │           │     │  │  ├─ CreateShipmentRequest.java
│  │  │  │           │     │  │  ├─ ShippingAddressRequest.java
│  │  │  │           │     │  │  └─ UpdateOrderShippingRequest.java
│  │  │  │           │     │  └─ shipmentResponse
│  │  │  │           │     │     └─ ShipmentResponse.java
│  │  │  │           │     ├─ entity
│  │  │  │           │     │  └─ OrderShipment.java
│  │  │  │           │     ├─ enums
│  │  │  │           │     │  └─ ShipmentStatus.java
│  │  │  │           │     ├─ mapper
│  │  │  │           │     │  └─ ShipmentMapper.java
│  │  │  │           │     ├─ repository
│  │  │  │           │     └─ service
│  │  │  │           │        ├─ impl
│  │  │  │           │        │  └─ ShipmentServiceImpl.java
│  │  │  │           │        └─ ShipmentService.java
│  │  │  │           ├─ payment_gateway
│  │  │  │           │  ├─ kpay
│  │  │  │           │  │  ├─ KPayClient.java
│  │  │  │           │  │  ├─ KPayGateway.java
│  │  │  │           │  │  ├─ KPayGatewayImpl.java
│  │  │  │           │  │  ├─ KPayRefundRequest.java
│  │  │  │           │  │  ├─ KPayRefundResponse.java
│  │  │  │           │  │  ├─ KPayRequest.java
│  │  │  │           │  │  ├─ KPayResponse.java
│  │  │  │           │  │  ├─ KPaySignatureValidator.java
│  │  │  │           │  │  ├─ KPayWebhookController.java
│  │  │  │           │  │  └─ KPayWebhookRequest.java
│  │  │  │           │  └─ stripe
│  │  │  │           ├─ product
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  ├─ ProductAttributeController.java
│  │  │  │           │  │  └─ ProductController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ AddAttributeValueRequest.java
│  │  │  │           │  │  │  ├─ CreateAttributeRequest.java
│  │  │  │           │  │  │  ├─ CreateProductRequest.java
│  │  │  │           │  │  │  ├─ CreateVariantRequest.java
│  │  │  │           │  │  │  ├─ UpdateAttributeRequest.java
│  │  │  │           │  │  │  ├─ UpdateAttributeValueRequest.java
│  │  │  │           │  │  │  └─ UpdateProductRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     ├─ ProductAttributeResponse.java
│  │  │  │           │  │     ├─ ProductResponse.java
│  │  │  │           │  │     └─ ProductVariantResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  ├─ Product.java
│  │  │  │           │  │  ├─ ProductAttribute.java
│  │  │  │           │  │  ├─ ProductAttributeValue.java
│  │  │  │           │  │  ├─ ProductImage.java
│  │  │  │           │  │  ├─ ProductSupplier.java
│  │  │  │           │  │  ├─ ProductVariant.java
│  │  │  │           │  │  ├─ ProductWarehouse.java
│  │  │  │           │  │  └─ Supplier.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  ├─ ProductMapper.java
│  │  │  │           │  │  └─ ProductVariantMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  ├─ ProductAttributeRepository.java
│  │  │  │           │  │  ├─ ProductAttributeValueRepository.java
│  │  │  │           │  │  ├─ ProductImageRepository.java
│  │  │  │           │  │  ├─ ProductRepository.java
│  │  │  │           │  │  ├─ ProductSupplierRepository.java
│  │  │  │           │  │  ├─ ProductVariantRepository.java
│  │  │  │           │  │  ├─ ProductWarehouseRepository.java
│  │  │  │           │  │  └─ SupplierRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ impl
│  │  │  │           │     │  ├─ ProductAttributeServiceImpl.java
│  │  │  │           │     │  └─ ProductServiceImpl.java
│  │  │  │           │     ├─ ProductAttributeService.java
│  │  │  │           │     └─ ProductService.java
│  │  │  │           ├─ store
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  └─ StoreController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ CreateStoreRequest.java
│  │  │  │           │  │  │  └─ UpdateStoreRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     └─ StoreResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  └─ Store.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  └─ StoreMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  └─ StoreRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ impl
│  │  │  │           │     │  └─ StoreServiceImpl.java
│  │  │  │           │     └─ StoreService.java
│  │  │  │           ├─ supplier
│  │  │  │           │  ├─ controller
│  │  │  │           │  │  └─ SupplierController.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ request
│  │  │  │           │  │  │  ├─ CreateSupplierRequest.java
│  │  │  │           │  │  │  └─ UpdateSupplierRequest.java
│  │  │  │           │  │  └─ response
│  │  │  │           │  │     └─ SupplierResponse.java
│  │  │  │           │  ├─ entity
│  │  │  │           │  │  └─ Supplier.java
│  │  │  │           │  ├─ mapper
│  │  │  │           │  │  └─ SupplierMapper.java
│  │  │  │           │  ├─ repository
│  │  │  │           │  │  └─ SupplierRepository.java
│  │  │  │           │  └─ service
│  │  │  │           │     ├─ impl
│  │  │  │           │     │  └─ SupplierServiceImpl.java
│  │  │  │           │     └─ SupplierService.java
│  │  │  │           └─ user
│  │  │  │              ├─ controller
│  │  │  │              │  └─ UserManagementController.java
│  │  │  │              ├─ dto
│  │  │  │              │  ├─ request
│  │  │  │              │  │  ├─ AssignRoleRequest.java
│  │  │  │              │  │  ├─ CreateUserRequest.java
│  │  │  │              │  │  └─ UpdateUserRequest.java
│  │  │  │              │  └─ response
│  │  │  │              │     └─ UserResponse.java
│  │  │  │              ├─ mapper
│  │  │  │              │  └─ UserMapper.java
│  │  │  │              ├─ repository
│  │  │  │              │  └─ UserManagementRepository.java
│  │  │  │              └─ service
│  │  │  │                 ├─ UserManagementService.java
│  │  │  │                 └─ UserManagementServiceImpl.java
│  │  │  └─ resources
│  │  │     ├─ application.properties
│  │  │     └─ db
│  │  │        └─ migration
│  │  │           ├─ V10__create_customers_table.sql
│  │  │           ├─ V11__create_inventory_table.sql
│  │  │           ├─ V12__create_inventory_transactions_table.sql
│  │  │           ├─ V13__create_purchase_orders_table.sql
│  │  │           ├─ V14__create_purchase_order_items_table.sql
│  │  │           ├─ V15__create_orders_table.sql
│  │  │           ├─ V16__create_order_items_table.sql
│  │  │           ├─ V17__create_payments_table.sql
│  │  │           ├─ V18__create_returns_table.sql
│  │  │           ├─ V19__create_audit_logs_table.sql
│  │  │           ├─ V1__create_stores_table.sql
│  │  │           ├─ V20__insert_initial_data.sql
│  │  │           ├─ V21__create_password_reset_tokens_table.sql
│  │  │           ├─ V22__create_otp_tokens_table.sql
│  │  │           ├─ V23__create_permissions_table.sql
│  │  │           ├─ V24__create_role_permissions_table.sql
│  │  │           ├─ V25__insert_role_permissions.sql
│  │  │           ├─ V26__improve_stores_table.sql
│  │  │           ├─ V27__add_category_columns.sql
│  │  │           ├─ V28__create_category_images_table.sql
│  │  │           ├─ V29__add_public_id_to_brands.sql
│  │  │           ├─ V2__create_users_table.sql
│  │  │           ├─ V30__create_product_tables.sql
│  │  │           ├─ V31__add_is_active_to_product_images_suppliers_warehouses.sql
│  │  │           ├─ V32__align_products_table_with_entity.sql
│  │  │           ├─ V3__create_roles_table.sql
│  │  │           ├─ V4__create_user_roles_table.sql
│  │  │           ├─ V5__create_categories_table.sql
│  │  │           ├─ V6__create_brands_table.sql
│  │  │           ├─ V7__create_suppliers_table.sql
│  │  │           ├─ V8__create_products_table.sql
│  │  │           └─ V9__create_product_suppliers_table.sql
│  │  └─ test
│  │     └─ java
│  │        └─ com
│  │           └─ example
│  │              └─ auth_system
│  │                 └─ AuthSystemApplicationTests.java
│  └─ target
│     ├─ classes
│     │  ├─ application.properties
│     │  ├─ com
│     │  │  └─ example
│     │  │     └─ auth_system
│     │  │        ├─ auth
│     │  │        │  ├─ controller
│     │  │        │  │  ├─ AuthController.class
│     │  │        │  │  └─ PermissionController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ ChangePasswordRequest.class
│     │  │        │  │  │  ├─ ForgotPasswordRequest.class
│     │  │        │  │  │  ├─ LoginRequest.class
│     │  │        │  │  │  ├─ RefreshTokenRequest.class
│     │  │        │  │  │  ├─ RegisterRequest.class
│     │  │        │  │  │  ├─ ResetPasswordRequest.class
│     │  │        │  │  │  ├─ SendOtpRequest.class
│     │  │        │  │  │  └─ VerifyOtpRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ AuthResponse$AuthResponseBuilder.class
│     │  │        │  │     ├─ AuthResponse.class
│     │  │        │  │     ├─ LoginResponse$LoginResponseBuilder.class
│     │  │        │  │     ├─ LoginResponse$UserInfo$UserInfoBuilder.class
│     │  │        │  │     ├─ LoginResponse$UserInfo.class
│     │  │        │  │     ├─ LoginResponse.class
│     │  │        │  │     ├─ RefreshTokenResponse$RefreshTokenResponseBuilder.class
│     │  │        │  │     ├─ RefreshTokenResponse.class
│     │  │        │  │     ├─ UserInfoResponse$UserInfoResponseBuilder.class
│     │  │        │  │     └─ UserInfoResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ OtpToken$OtpTokenBuilder.class
│     │  │        │  │  ├─ OtpToken.class
│     │  │        │  │  ├─ OtpType.class
│     │  │        │  │  ├─ PasswordResetToken$PasswordResetTokenBuilder.class
│     │  │        │  │  ├─ PasswordResetToken.class
│     │  │        │  │  ├─ Permission$PermissionBuilder.class
│     │  │        │  │  ├─ Permission.class
│     │  │        │  │  ├─ Role$RoleBuilder.class
│     │  │        │  │  ├─ Role.class
│     │  │        │  │  ├─ RoleName.class
│     │  │        │  │  ├─ User$UserBuilder.class
│     │  │        │  │  └─ User.class
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ OtpTokenRepository.class
│     │  │        │  │  ├─ PasswordResetTokenRepository.class
│     │  │        │  │  ├─ PermissionRepository.class
│     │  │        │  │  ├─ RolePermissionRepository.class
│     │  │        │  │  ├─ RoleRepository.class
│     │  │        │  │  └─ UserRepository.class
│     │  │        │  ├─ security
│     │  │        │  │  ├─ CustomUserDetails.class
│     │  │        │  │  ├─ CustomUserDetailsService.class
│     │  │        │  │  ├─ JwtAuthenticationFilter.class
│     │  │        │  │  ├─ JwtTokenProvider.class
│     │  │        │  │  └─ PermissionEvaluator.class
│     │  │        │  └─ service
│     │  │        │     ├─ AuthService.class
│     │  │        │     ├─ AuthServiceImpl.class
│     │  │        │     ├─ OtpService.class
│     │  │        │     ├─ PermissionManagementService.class
│     │  │        │     ├─ PermissionManagementServiceImpl.class
│     │  │        │     ├─ PermissionService.class
│     │  │        │     └─ PermissionServiceImpl.class
│     │  │        ├─ AuthSystemApplication.class
│     │  │        ├─ brand
│     │  │        │  ├─ controller
│     │  │        │  │  └─ BrandController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ CreateBrandRequest$CreateBrandRequestBuilder.class
│     │  │        │  │  │  ├─ CreateBrandRequest.class
│     │  │        │  │  │  ├─ UpdateBrandRequest$UpdateBrandRequestBuilder.class
│     │  │        │  │  │  └─ UpdateBrandRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ BrandResponse$BrandResponseBuilder.class
│     │  │        │  │     └─ BrandResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ Brand$BrandBuilder.class
│     │  │        │  │  └─ Brand.class
│     │  │        │  ├─ mapper
│     │  │        │  │  └─ BrandMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  └─ BrandRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ BrandService.class
│     │  │        │     └─ impl
│     │  │        │        └─ BrandServiceImpl.class
│     │  │        ├─ category
│     │  │        │  ├─ controller
│     │  │        │  │  └─ CategoryController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ CategoryImageRequest$CategoryImageRequestBuilder.class
│     │  │        │  │  │  ├─ CategoryImageRequest.class
│     │  │        │  │  │  ├─ CreateCategoryRequest$CreateCategoryRequestBuilder.class
│     │  │        │  │  │  ├─ CreateCategoryRequest.class
│     │  │        │  │  │  ├─ UpdateCategoryRequest$UpdateCategoryRequestBuilder.class
│     │  │        │  │  │  └─ UpdateCategoryRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ CategoryResponse$CategoryImageResponse$CategoryImageResponseBuilder.class
│     │  │        │  │     ├─ CategoryResponse$CategoryImageResponse.class
│     │  │        │  │     ├─ CategoryResponse$CategoryResponseBuilder.class
│     │  │        │  │     └─ CategoryResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ Category$CategoryBuilder.class
│     │  │        │  │  ├─ Category.class
│     │  │        │  │  ├─ CategoryImage$CategoryImageBuilder.class
│     │  │        │  │  └─ CategoryImage.class
│     │  │        │  ├─ mapper
│     │  │        │  │  └─ CategoryMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ CategoryImageRepository.class
│     │  │        │  │  └─ CategoryRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ CategoryService.class
│     │  │        │     └─ impl
│     │  │        │        └─ CategoryServiceImpl.class
│     │  │        ├─ common
│     │  │        │  ├─ bootstrap
│     │  │        │  │  └─ AdminInitializer.class
│     │  │        │  ├─ config
│     │  │        │  │  ├─ CloudinaryConfig.class
│     │  │        │  │  ├─ RestTemplateConfig.class
│     │  │        │  │  ├─ SecurityConfig.class
│     │  │        │  │  ├─ SwaggerConfig.class
│     │  │        │  │  └─ WebConfig.class
│     │  │        │  ├─ dto
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ ApiResponse$ApiResponseBuilder.class
│     │  │        │  │     ├─ ApiResponse.class
│     │  │        │  │     └─ PageResponse.class
│     │  │        │  ├─ exception
│     │  │        │  │  ├─ AuthException.class
│     │  │        │  │  ├─ BusinessException.class
│     │  │        │  │  ├─ ErrorResponse$ErrorResponseBuilder.class
│     │  │        │  │  ├─ ErrorResponse.class
│     │  │        │  │  ├─ GlobalExceptionHandler.class
│     │  │        │  │  ├─ InvalidCredentialsException.class
│     │  │        │  │  ├─ InvalidTokenException.class
│     │  │        │  │  ├─ OtpValidationException.class
│     │  │        │  │  ├─ ResourceNotFoundException.class
│     │  │        │  │  └─ UserAlreadyExistsException.class
│     │  │        │  ├─ service
│     │  │        │  │  ├─ CloudinaryService.class
│     │  │        │  │  ├─ CurrentUserService.class
│     │  │        │  │  ├─ EmailService$1.class
│     │  │        │  │  └─ EmailService.class
│     │  │        │  └─ util
│     │  │        │     ├─ DateUtils.class
│     │  │        │     ├─ JwtUtil.class
│     │  │        │     ├─ OtpGenerator.class
│     │  │        │     └─ ValidationUtils.class
│     │  │        ├─ customer
│     │  │        │  ├─ controller
│     │  │        │  │  ├─ CustomerController.class
│     │  │        │  │  ├─ CustomerGroupController.class
│     │  │        │  │  └─ WishlistController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ AddWishlistItemRequest$AddWishlistItemRequestBuilder.class
│     │  │        │  │  │  ├─ AddWishlistItemRequest.class
│     │  │        │  │  │  ├─ CreateCustomerGroupRequest$CreateCustomerGroupRequestBuilder.class
│     │  │        │  │  │  ├─ CreateCustomerGroupRequest.class
│     │  │        │  │  │  ├─ CreateCustomerRequest$CreateCustomerRequestBuilder.class
│     │  │        │  │  │  ├─ CreateCustomerRequest.class
│     │  │        │  │  │  ├─ CreateWishlistRequest$CreateWishlistRequestBuilder.class
│     │  │        │  │  │  ├─ CreateWishlistRequest.class
│     │  │        │  │  │  ├─ UpdateCustomerGroupRequest$UpdateCustomerGroupRequestBuilder.class
│     │  │        │  │  │  ├─ UpdateCustomerGroupRequest.class
│     │  │        │  │  │  ├─ UpdateCustomerRequest$UpdateCustomerRequestBuilder.class
│     │  │        │  │  │  ├─ UpdateCustomerRequest.class
│     │  │        │  │  │  ├─ UpdateWishlistRequest$UpdateWishlistRequestBuilder.class
│     │  │        │  │  │  └─ UpdateWishlistRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ CustomerGroupResponse$CustomerGroupResponseBuilder.class
│     │  │        │  │     ├─ CustomerGroupResponse.class
│     │  │        │  │     ├─ CustomerResponse$CustomerResponseBuilder.class
│     │  │        │  │     ├─ CustomerResponse.class
│     │  │        │  │     ├─ WishlistResponse$WishlistItemResponse$WishlistItemResponseBuilder.class
│     │  │        │  │     ├─ WishlistResponse$WishlistItemResponse.class
│     │  │        │  │     ├─ WishlistResponse$WishlistResponseBuilder.class
│     │  │        │  │     └─ WishlistResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ Customer$CustomerBuilder.class
│     │  │        │  │  ├─ Customer.class
│     │  │        │  │  ├─ CustomerGroup$CustomerGroupBuilder.class
│     │  │        │  │  ├─ CustomerGroup.class
│     │  │        │  │  ├─ Wishlist$WishlistBuilder.class
│     │  │        │  │  ├─ Wishlist.class
│     │  │        │  │  ├─ WishlistItem$WishlistItemBuilder.class
│     │  │        │  │  └─ WishlistItem.class
│     │  │        │  ├─ enums
│     │  │        │  │  └─ Gender.class
│     │  │        │  ├─ mapper
│     │  │        │  │  ├─ CustomerGroupMapper.class
│     │  │        │  │  ├─ CustomerMapper.class
│     │  │        │  │  └─ WishlistMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ CustomerGroupRepository.class
│     │  │        │  │  ├─ CustomerRepository.class
│     │  │        │  │  ├─ WishlistItemRepository.class
│     │  │        │  │  └─ WishlistRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ CustomerGroupService.class
│     │  │        │     ├─ CustomerService.class
│     │  │        │     ├─ impl
│     │  │        │     │  ├─ CustomerGroupServiceImpl.class
│     │  │        │     │  ├─ CustomerServiceImpl.class
│     │  │        │     │  └─ WishlistServiceImpl.class
│     │  │        │     └─ WishlistService.class
│     │  │        ├─ inventory
│     │  │        │  ├─ controller
│     │  │        │  │  ├─ InventoryCountController.class
│     │  │        │  │  ├─ InventoryCountItemController.class
│     │  │        │  │  ├─ StockAdjustmentController.class
│     │  │        │  │  ├─ StockMovementController.class
│     │  │        │  │  └─ WarehouseStockController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ inventoryCount
│     │  │        │  │  │  │  ├─ CompleteInventoryCountRequest$CompleteInventoryCountRequestBuilder.class
│     │  │        │  │  │  │  ├─ CompleteInventoryCountRequest.class
│     │  │        │  │  │  │  ├─ CreateInventoryCountItemRequest$CreateInventoryCountItemRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreateInventoryCountItemRequest.class
│     │  │        │  │  │  │  ├─ CreateInventoryCountRequest$CreateInventoryCountRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreateInventoryCountRequest.class
│     │  │        │  │  │  │  ├─ UpdateCountedQuantityRequest$UpdateCountedQuantityRequestBuilder.class
│     │  │        │  │  │  │  ├─ UpdateCountedQuantityRequest.class
│     │  │        │  │  │  │  ├─ UpdateInventoryCountItemsRequest$UpdateInventoryCountItemsRequestBuilder.class
│     │  │        │  │  │  │  ├─ UpdateInventoryCountItemsRequest.class
│     │  │        │  │  │  │  ├─ UpdateInventoryCountRequest$UpdateInventoryCountRequestBuilder.class
│     │  │        │  │  │  │  ├─ UpdateInventoryCountRequest.class
│     │  │        │  │  │  │  ├─ VerifyInventoryCountRequest$VerifyInventoryCountRequestBuilder.class
│     │  │        │  │  │  │  └─ VerifyInventoryCountRequest.class
│     │  │        │  │  │  ├─ stockAdjustment
│     │  │        │  │  │  │  ├─ ApproveStockAdjustmentRequest$ApproveStockAdjustmentRequestBuilder.class
│     │  │        │  │  │  │  ├─ ApproveStockAdjustmentRequest.class
│     │  │        │  │  │  │  ├─ CreateStockAdjustmentRequest$CreateStockAdjustmentRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreateStockAdjustmentRequest.class
│     │  │        │  │  │  │  ├─ UpdateStockAdjustmentRequest$UpdateStockAdjustmentRequestBuilder.class
│     │  │        │  │  │  │  └─ UpdateStockAdjustmentRequest.class
│     │  │        │  │  │  └─ stockMovement
│     │  │        │  │  │     ├─ CreateStockMovementRequest$CreateStockMovementRequestBuilder.class
│     │  │        │  │  │     ├─ CreateStockMovementRequest.class
│     │  │        │  │  │     ├─ TransferStockRequest$TransferStockRequestBuilder.class
│     │  │        │  │  │     └─ TransferStockRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ inventoryCount
│     │  │        │  │     │  ├─ InventoryCountDetailResponse$InventoryCountDetailResponseBuilder.class
│     │  │        │  │     │  ├─ InventoryCountDetailResponse.class
│     │  │        │  │     │  ├─ InventoryCountItemResponse$InventoryCountItemResponseBuilder.class
│     │  │        │  │     │  ├─ InventoryCountItemResponse.class
│     │  │        │  │     │  ├─ InventoryCountResponse$InventoryCountResponseBuilder.class
│     │  │        │  │     │  └─ InventoryCountResponse.class
│     │  │        │  │     ├─ InventoryDashboardResponse$InventoryDashboardResponseBuilder.class
│     │  │        │  │     ├─ InventoryDashboardResponse.class
│     │  │        │  │     ├─ LowStockResponse$LowStockResponseBuilder.class
│     │  │        │  │     ├─ LowStockResponse.class
│     │  │        │  │     ├─ StockAdjustmentResponse$StockAdjustmentResponseBuilder.class
│     │  │        │  │     ├─ StockAdjustmentResponse.class
│     │  │        │  │     ├─ StockAdjustmentSummaryResponse$StockAdjustmentSummaryResponseBuilder.class
│     │  │        │  │     ├─ StockAdjustmentSummaryResponse.class
│     │  │        │  │     ├─ StockInOutResponse$StockInOutResponseBuilder.class
│     │  │        │  │     ├─ StockInOutResponse.class
│     │  │        │  │     ├─ StockMovementResponse$StockMovementResponseBuilder.class
│     │  │        │  │     ├─ StockMovementResponse.class
│     │  │        │  │     ├─ StockMovementSummaryResponse$StockMovementSummaryResponseBuilder.class
│     │  │        │  │     ├─ StockMovementSummaryResponse.class
│     │  │        │  │     ├─ StockSummaryResponse$StockSummaryResponseBuilder.class
│     │  │        │  │     ├─ StockSummaryResponse.class
│     │  │        │  │     ├─ WarehouseStockResponse$WarehouseStockResponseBuilder.class
│     │  │        │  │     └─ WarehouseStockResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ InventoryCount$InventoryCountBuilder.class
│     │  │        │  │  ├─ InventoryCount.class
│     │  │        │  │  ├─ InventoryCountItem$InventoryCountItemBuilder.class
│     │  │        │  │  ├─ InventoryCountItem.class
│     │  │        │  │  ├─ StockAdjustment$StockAdjustmentBuilder.class
│     │  │        │  │  ├─ StockAdjustment.class
│     │  │        │  │  ├─ StockMovement$StockMovementBuilder.class
│     │  │        │  │  ├─ StockMovement.class
│     │  │        │  │  ├─ WarehouseStock$WarehouseStockBuilder.class
│     │  │        │  │  └─ WarehouseStock.class
│     │  │        │  ├─ enums
│     │  │        │  │  ├─ AdjustmentDirection.class
│     │  │        │  │  ├─ AdjustmentStatus.class
│     │  │        │  │  ├─ AdjustmentType.class
│     │  │        │  │  ├─ CountType.class
│     │  │        │  │  ├─ InventoryCountStatus.class
│     │  │        │  │  ├─ MovementType.class
│     │  │        │  │  ├─ ReferenceType.class
│     │  │        │  │  └─ StockStatus.class
│     │  │        │  ├─ mapper
│     │  │        │  │  ├─ InventoryCountItemMapper.class
│     │  │        │  │  ├─ InventoryCountMapper.class
│     │  │        │  │  ├─ StockAdjustmentMapper.class
│     │  │        │  │  ├─ StockMovementMapper.class
│     │  │        │  │  └─ WarehouseStockMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ InventoryCountItemRepository.class
│     │  │        │  │  ├─ InventoryCountReportRepository.class
│     │  │        │  │  ├─ InventoryCountRepository.class
│     │  │        │  │  ├─ projection
│     │  │        │  │  │  └─ StockAdjustmentSummaryProjection.class
│     │  │        │  │  ├─ StockAdjustmentRepository.class
│     │  │        │  │  ├─ StockMovementRepository.class
│     │  │        │  │  └─ WarehouseStockRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ impl
│     │  │        │     │  ├─ InventoryCountItemServiceImpl.class
│     │  │        │     │  ├─ InventoryCountServiceImpl.class
│     │  │        │     │  ├─ StockAdjustmentServiceImpl.class
│     │  │        │     │  ├─ StockMovementSerciveImpl$1.class
│     │  │        │     │  ├─ StockMovementSerciveImpl.class
│     │  │        │     │  └─ WarehouseStockServiceImpl.class
│     │  │        │     ├─ InventoryCountItemService.class
│     │  │        │     ├─ InventoryCountService.class
│     │  │        │     ├─ StockAdjustmentService.class
│     │  │        │     ├─ StockMovementService.class
│     │  │        │     └─ WarehouseStockService.class
│     │  │        ├─ order
│     │  │        │  ├─ order
│     │  │        │  │  ├─ controller
│     │  │        │  │  │  └─ OrderController.class
│     │  │        │  │  ├─ dto
│     │  │        │  │  │  ├─ orderRequest
│     │  │        │  │  │  │  ├─ CreateOrderItemRequest$CreateOrderItemRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreateOrderItemRequest.class
│     │  │        │  │  │  │  ├─ CreateOrderRequest$CreateOrderRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreateOrderRequest.class
│     │  │        │  │  │  │  ├─ UpdateOrderInternalNoteRequest$UpdateOrderInternalNoteRequestBuilder.class
│     │  │        │  │  │  │  ├─ UpdateOrderInternalNoteRequest.class
│     │  │        │  │  │  │  ├─ UpdateOrderPricingRequest$UpdateOrderPricingRequestBuilder.class
│     │  │        │  │  │  │  ├─ UpdateOrderPricingRequest.class
│     │  │        │  │  │  │  ├─ UpdateOrderRequest$UpdateOrderRequestBuilder.class
│     │  │        │  │  │  │  ├─ UpdateOrderRequest.class
│     │  │        │  │  │  │  ├─ UpdateOrderStatusRequest$UpdateOrderStatusRequestBuilder.class
│     │  │        │  │  │  │  └─ UpdateOrderStatusRequest.class
│     │  │        │  │  │  └─ orderResponse
│     │  │        │  │  │     ├─ AddressInfoResponse$AddressInfoResponseBuilder.class
│     │  │        │  │  │     ├─ AddressInfoResponse.class
│     │  │        │  │  │     ├─ CustomerInfoResponse$CustomerInfoResponseBuilder.class
│     │  │        │  │  │     ├─ CustomerInfoResponse.class
│     │  │        │  │  │     ├─ OrderItemResponse$OrderItemResponseBuilder.class
│     │  │        │  │  │     ├─ OrderItemResponse.class
│     │  │        │  │  │     ├─ OrderResponse$OrderResponseBuilder.class
│     │  │        │  │  │     ├─ OrderResponse.class
│     │  │        │  │  │     ├─ OrderSummaryResponse$OrderSummaryResponseBuilder.class
│     │  │        │  │  │     ├─ OrderSummaryResponse.class
│     │  │        │  │  │     ├─ OrderTimelineResponse$OrderTimelineResponseBuilder.class
│     │  │        │  │  │     ├─ OrderTimelineResponse.class
│     │  │        │  │  │     ├─ UserInfoResponse$UserInfoResponseBuilder.class
│     │  │        │  │  │     └─ UserInfoResponse.class
│     │  │        │  │  ├─ entity
│     │  │        │  │  │  ├─ Order$OrderBuilder.class
│     │  │        │  │  │  ├─ Order.class
│     │  │        │  │  │  ├─ OrderItem$OrderItemBuilder.class
│     │  │        │  │  │  ├─ OrderItem.class
│     │  │        │  │  │  ├─ OrderStatusHistory$OrderStatusHistoryBuilder.class
│     │  │        │  │  │  └─ OrderStatusHistory.class
│     │  │        │  │  ├─ enums
│     │  │        │  │  │  ├─ FulfillmentStatus.class
│     │  │        │  │  │  ├─ OrderStatus.class
│     │  │        │  │  │  └─ OrderType.class
│     │  │        │  │  ├─ mapper
│     │  │        │  │  │  ├─ OrderItemMapper.class
│     │  │        │  │  │  └─ OrderMapper.class
│     │  │        │  │  ├─ repository
│     │  │        │  │  │  ├─ OrderItemRepository.class
│     │  │        │  │  │  ├─ OrderRepository.class
│     │  │        │  │  │  ├─ OrderShipmentRepository.class
│     │  │        │  │  │  └─ OrderStatusHistoryRepository.class
│     │  │        │  │  └─ service
│     │  │        │  │     ├─ impl
│     │  │        │  │     │  ├─ OrderServiceImpl.class
│     │  │        │  │     │  ├─ OrderStatusServiceImpl$1.class
│     │  │        │  │     │  └─ OrderStatusServiceImpl.class
│     │  │        │  │     ├─ OrderService.class
│     │  │        │  │     └─ OrderStatusService.class
│     │  │        │  ├─ payment
│     │  │        │  │  ├─ controller
│     │  │        │  │  │  └─ PaymentController.class
│     │  │        │  │  ├─ dto
│     │  │        │  │  │  ├─ paymentRequest
│     │  │        │  │  │  │  ├─ CreateEcommercePaymentRequest$CreateEcommercePaymentRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreateEcommercePaymentRequest.class
│     │  │        │  │  │  │  ├─ CreatePaymentRequest$CreatePaymentRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreatePaymentRequest.class
│     │  │        │  │  │  │  ├─ CreatePosPaymentRequest$CreatePosPaymentRequestBuilder.class
│     │  │        │  │  │  │  ├─ CreatePosPaymentRequest.class
│     │  │        │  │  │  │  ├─ PaymentWebhookRequest$PaymentWebhookRequestBuilder.class
│     │  │        │  │  │  │  └─ PaymentWebhookRequest.class
│     │  │        │  │  │  └─ paymentResponse
│     │  │        │  │  │     ├─ EcommercePaymentResponse$EcommercePaymentResponseBuilder.class
│     │  │        │  │  │     ├─ EcommercePaymentResponse.class
│     │  │        │  │  │     ├─ PaymentResponse$PaymentResponseBuilder.class
│     │  │        │  │  │     └─ PaymentResponse.class
│     │  │        │  │  ├─ entity
│     │  │        │  │  │  ├─ Payment$PaymentBuilder.class
│     │  │        │  │  │  └─ Payment.class
│     │  │        │  │  ├─ enums
│     │  │        │  │  │  ├─ PaymentMethod.class
│     │  │        │  │  │  └─ PaymentStatus.class
│     │  │        │  │  ├─ mapper
│     │  │        │  │  │  └─ PaymentMapper.class
│     │  │        │  │  ├─ repository
│     │  │        │  │  │  └─ PaymentRepository.class
│     │  │        │  │  └─ service
│     │  │        │  │     ├─ impl
│     │  │        │  │     │  └─ PaymentServiceImpl.class
│     │  │        │  │     └─ PaymentService.class
│     │  │        │  ├─ refund
│     │  │        │  │  ├─ dto
│     │  │        │  │  │  ├─ refundRequest
│     │  │        │  │  │  │  ├─ ProcessRefundRequest$ProcessRefundRequestBuilder.class
│     │  │        │  │  │  │  ├─ ProcessRefundRequest.class
│     │  │        │  │  │  │  ├─ RefundItemRequest$RefundItemRequestBuilder.class
│     │  │        │  │  │  │  └─ RefundItemRequest.class
│     │  │        │  │  │  └─ refundResponse
│     │  │        │  │  │     ├─ RefundItemResponse$RefundItemResponseBuilder.class
│     │  │        │  │  │     ├─ RefundItemResponse.class
│     │  │        │  │  │     ├─ RefundResponse$RefundResponseBuilder.class
│     │  │        │  │  │     └─ RefundResponse.class
│     │  │        │  │  ├─ entity
│     │  │        │  │  │  ├─ Notification$NotificationBuilder.class
│     │  │        │  │  │  ├─ Notification.class
│     │  │        │  │  │  ├─ Refund$RefundBuilder.class
│     │  │        │  │  │  ├─ Refund.class
│     │  │        │  │  │  ├─ RefundItem$RefundItemBuilder.class
│     │  │        │  │  │  └─ RefundItem.class
│     │  │        │  │  ├─ enums
│     │  │        │  │  │  ├─ RefundMethod.class
│     │  │        │  │  │  ├─ RefundStatus.class
│     │  │        │  │  │  └─ RefundType.class
│     │  │        │  │  ├─ mapper
│     │  │        │  │  │  ├─ RefundItemMapper.class
│     │  │        │  │  │  └─ RefundMapper.class
│     │  │        │  │  ├─ repository
│     │  │        │  │  │  ├─ NotificationRepository.class
│     │  │        │  │  │  ├─ RefundItemRepository.class
│     │  │        │  │  │  └─ RefundRepository.class
│     │  │        │  │  └─ service
│     │  │        │  │     ├─ impl
│     │  │        │  │     │  └─ RefundServiceImpl.class
│     │  │        │  │     └─ RefundService.class
│     │  │        │  └─ shipment
│     │  │        │     ├─ dto
│     │  │        │     │  ├─ shipmentRequest
│     │  │        │     │  │  ├─ BillingAddressRequest$BillingAddressRequestBuilder.class
│     │  │        │     │  │  ├─ BillingAddressRequest.class
│     │  │        │     │  │  ├─ CreateShipmentRequest$CreateShipmentRequestBuilder.class
│     │  │        │     │  │  ├─ CreateShipmentRequest.class
│     │  │        │     │  │  ├─ ShippingAddressRequest$ShippingAddressRequestBuilder.class
│     │  │        │     │  │  ├─ ShippingAddressRequest.class
│     │  │        │     │  │  ├─ UpdateOrderShippingRequest$UpdateOrderShippingRequestBuilder.class
│     │  │        │     │  │  └─ UpdateOrderShippingRequest.class
│     │  │        │     │  └─ shipmentResponse
│     │  │        │     │     ├─ ShipmentResponse$ShipmentResponseBuilder.class
│     │  │        │     │     └─ ShipmentResponse.class
│     │  │        │     ├─ entity
│     │  │        │     │  ├─ OrderShipment$OrderShipmentBuilder.class
│     │  │        │     │  └─ OrderShipment.class
│     │  │        │     ├─ enums
│     │  │        │     │  └─ ShipmentStatus.class
│     │  │        │     ├─ mapper
│     │  │        │     │  └─ ShipmentMapper.class
│     │  │        │     └─ service
│     │  │        │        ├─ impl
│     │  │        │        │  ├─ ShipmentServiceImpl$1.class
│     │  │        │        │  └─ ShipmentServiceImpl.class
│     │  │        │        └─ ShipmentService.class
│     │  │        ├─ payment_gateway
│     │  │        │  └─ kpay
│     │  │        │     ├─ KPayClient.class
│     │  │        │     ├─ KPayGateway.class
│     │  │        │     ├─ KPayGatewayImpl.class
│     │  │        │     ├─ KPayRefundRequest$KPayRefundRequestBuilder.class
│     │  │        │     ├─ KPayRefundRequest.class
│     │  │        │     ├─ KPayRefundResponse$KPayRefundResponseBuilder.class
│     │  │        │     ├─ KPayRefundResponse.class
│     │  │        │     ├─ KPayRequest$KPayRequestBuilder.class
│     │  │        │     ├─ KPayRequest.class
│     │  │        │     ├─ KPayResponse.class
│     │  │        │     ├─ KPaySignatureValidator.class
│     │  │        │     ├─ KPayWebhookController.class
│     │  │        │     ├─ KPayWebhookRequest$KPayWebhookRequestBuilder.class
│     │  │        │     └─ KPayWebhookRequest.class
│     │  │        ├─ product
│     │  │        │  ├─ controller
│     │  │        │  │  ├─ ProductAttributeController.class
│     │  │        │  │  └─ ProductController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ AddAttributeValueRequest$AddAttributeValueRequestBuilder.class
│     │  │        │  │  │  ├─ AddAttributeValueRequest.class
│     │  │        │  │  │  ├─ CreateAttributeRequest$CreateAttributeRequestBuilder.class
│     │  │        │  │  │  ├─ CreateAttributeRequest.class
│     │  │        │  │  │  ├─ CreateProductRequest$CreateProductRequestBuilder.class
│     │  │        │  │  │  ├─ CreateProductRequest$CreateVariantRequest$CreateVariantRequestBuilder.class
│     │  │        │  │  │  ├─ CreateProductRequest$CreateVariantRequest.class
│     │  │        │  │  │  ├─ CreateProductRequest$ImageRequest$ImageRequestBuilder.class
│     │  │        │  │  │  ├─ CreateProductRequest$ImageRequest.class
│     │  │        │  │  │  ├─ CreateProductRequest$SupplierRequest$SupplierRequestBuilder.class
│     │  │        │  │  │  ├─ CreateProductRequest$SupplierRequest.class
│     │  │        │  │  │  ├─ CreateProductRequest$VariantImageRequest$VariantImageRequestBuilder.class
│     │  │        │  │  │  ├─ CreateProductRequest$VariantImageRequest.class
│     │  │        │  │  │  ├─ CreateProductRequest$WarehouseStockRequest$WarehouseStockRequestBuilder.class
│     │  │        │  │  │  ├─ CreateProductRequest$WarehouseStockRequest.class
│     │  │        │  │  │  ├─ CreateProductRequest.class
│     │  │        │  │  │  ├─ CreateVariantRequest.class
│     │  │        │  │  │  ├─ UpdateAttributeRequest$UpdateAttributeRequestBuilder.class
│     │  │        │  │  │  ├─ UpdateAttributeRequest.class
│     │  │        │  │  │  ├─ UpdateAttributeValueRequest$UpdateAttributeValueRequestBuilder.class
│     │  │        │  │  │  ├─ UpdateAttributeValueRequest.class
│     │  │        │  │  │  ├─ UpdateProductRequest$UpdateProductRequestBuilder.class
│     │  │        │  │  │  └─ UpdateProductRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ ProductAttributeResponse$AttributeValueResponse$AttributeValueResponseBuilder.class
│     │  │        │  │     ├─ ProductAttributeResponse$AttributeValueResponse.class
│     │  │        │  │     ├─ ProductAttributeResponse$ProductAttributeResponseBuilder.class
│     │  │        │  │     ├─ ProductAttributeResponse.class
│     │  │        │  │     ├─ ProductResponse$ProductImageResponse$ProductImageResponseBuilder.class
│     │  │        │  │     ├─ ProductResponse$ProductImageResponse.class
│     │  │        │  │     ├─ ProductResponse$ProductResponseBuilder.class
│     │  │        │  │     ├─ ProductResponse$ProductSupplierResponse$ProductSupplierResponseBuilder.class
│     │  │        │  │     ├─ ProductResponse$ProductSupplierResponse.class
│     │  │        │  │     ├─ ProductResponse$ProductVariantResponse$ProductVariantResponseBuilder.class
│     │  │        │  │     ├─ ProductResponse$ProductVariantResponse.class
│     │  │        │  │     ├─ ProductResponse.class
│     │  │        │  │     ├─ ProductVariantResponse$ProductVariantResponseBuilder.class
│     │  │        │  │     └─ ProductVariantResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ Product$ProductBuilder.class
│     │  │        │  │  ├─ Product.class
│     │  │        │  │  ├─ ProductAttribute$AttributeType.class
│     │  │        │  │  ├─ ProductAttribute$ProductAttributeBuilder.class
│     │  │        │  │  ├─ ProductAttribute.class
│     │  │        │  │  ├─ ProductAttributeValue$ProductAttributeValueBuilder.class
│     │  │        │  │  ├─ ProductAttributeValue.class
│     │  │        │  │  ├─ ProductImage$ProductImageBuilder.class
│     │  │        │  │  ├─ ProductImage.class
│     │  │        │  │  ├─ ProductSupplier$ProductSupplierBuilder.class
│     │  │        │  │  ├─ ProductSupplier.class
│     │  │        │  │  ├─ ProductVariant$ProductVariantBuilder.class
│     │  │        │  │  ├─ ProductVariant.class
│     │  │        │  │  ├─ ProductWarehouse$ProductWarehouseBuilder.class
│     │  │        │  │  └─ ProductWarehouse.class
│     │  │        │  ├─ mapper
│     │  │        │  │  ├─ ProductMapper.class
│     │  │        │  │  └─ ProductVariantMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ ProductAttributeRepository.class
│     │  │        │  │  ├─ ProductAttributeValueRepository.class
│     │  │        │  │  ├─ ProductImageRepository.class
│     │  │        │  │  ├─ ProductRepository.class
│     │  │        │  │  ├─ ProductSupplierRepository.class
│     │  │        │  │  ├─ ProductVariantRepository.class
│     │  │        │  │  └─ ProductWarehouseRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ impl
│     │  │        │     │  ├─ ProductAttributeServiceImpl.class
│     │  │        │     │  └─ ProductServiceImpl.class
│     │  │        │     ├─ ProductAttributeService.class
│     │  │        │     └─ ProductService.class
│     │  │        ├─ store
│     │  │        │  ├─ controller
│     │  │        │  │  └─ StoreController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ CreateStoreRequest$CreateStoreRequestBuilder.class
│     │  │        │  │  │  ├─ CreateStoreRequest.class
│     │  │        │  │  │  ├─ UpdateStoreRequest$UpdateStoreRequestBuilder.class
│     │  │        │  │  │  └─ UpdateStoreRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ StoreResponse$StoreResponseBuilder.class
│     │  │        │  │     └─ StoreResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ Store$StoreBuilder.class
│     │  │        │  │  └─ Store.class
│     │  │        │  ├─ mapper
│     │  │        │  │  └─ StoreMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  └─ StoreRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ impl
│     │  │        │     │  └─ StoreServiceImpl.class
│     │  │        │     └─ StoreService.class
│     │  │        ├─ supplier
│     │  │        │  ├─ controller
│     │  │        │  │  └─ SupplierController.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ request
│     │  │        │  │  │  ├─ CreateSupplierRequest$CreateSupplierRequestBuilder.class
│     │  │        │  │  │  ├─ CreateSupplierRequest.class
│     │  │        │  │  │  ├─ UpdateSupplierRequest$UpdateSupplierRequestBuilder.class
│     │  │        │  │  │  └─ UpdateSupplierRequest.class
│     │  │        │  │  └─ response
│     │  │        │  │     ├─ SupplierResponse$SupplierResponseBuilder.class
│     │  │        │  │     └─ SupplierResponse.class
│     │  │        │  ├─ entity
│     │  │        │  │  ├─ Supplier$SupplierBuilder.class
│     │  │        │  │  └─ Supplier.class
│     │  │        │  ├─ mapper
│     │  │        │  │  └─ SupplierMapper.class
│     │  │        │  ├─ repository
│     │  │        │  │  └─ SupplierRepository.class
│     │  │        │  └─ service
│     │  │        │     ├─ impl
│     │  │        │     │  └─ SupplierServiceImpl.class
│     │  │        │     └─ SupplierService.class
│     │  │        └─ user
│     │  │           ├─ controller
│     │  │           │  └─ UserManagementController.class
│     │  │           ├─ dto
│     │  │           │  ├─ request
│     │  │           │  │  ├─ AssignRoleRequest$AssignRoleRequestBuilder.class
│     │  │           │  │  ├─ AssignRoleRequest.class
│     │  │           │  │  ├─ CreateUserRequest$CreateUserRequestBuilder.class
│     │  │           │  │  ├─ CreateUserRequest.class
│     │  │           │  │  ├─ UpdateUserRequest$UpdateUserRequestBuilder.class
│     │  │           │  │  └─ UpdateUserRequest.class
│     │  │           │  └─ response
│     │  │           │     ├─ UserResponse$UserResponseBuilder.class
│     │  │           │     └─ UserResponse.class
│     │  │           ├─ mapper
│     │  │           │  └─ UserMapper.class
│     │  │           ├─ repository
│     │  │           │  └─ UserManagementRepository.class
│     │  │           └─ service
│     │  │              ├─ UserManagementService.class
│     │  │              └─ UserManagementServiceImpl.class
│     │  └─ db
│     │     └─ migration
│     │        ├─ V10__create_customers_table.sql
│     │        ├─ V11__create_inventory_table.sql
│     │        ├─ V12__create_inventory_transactions_table.sql
│     │        ├─ V13__create_purchase_orders_table.sql
│     │        ├─ V14__create_purchase_order_items_table.sql
│     │        ├─ V15__create_orders_table.sql
│     │        ├─ V16__create_order_items_table.sql
│     │        ├─ V17__create_payments_table.sql
│     │        ├─ V18__create_returns_table.sql
│     │        ├─ V19__create_audit_logs_table.sql
│     │        ├─ V1__create_stores_table.sql
│     │        ├─ V20__insert_initial_data.sql
│     │        ├─ V21__create_password_reset_tokens_table.sql
│     │        ├─ V22__create_otp_tokens_table.sql
│     │        ├─ V23__create_permissions_table.sql
│     │        ├─ V24__create_role_permissions_table.sql
│     │        ├─ V25__insert_role_permissions.sql
│     │        ├─ V26__improve_stores_table.sql
│     │        ├─ V27__add_category_columns.sql
│     │        ├─ V28__create_category_images_table.sql
│     │        ├─ V29__add_public_id_to_brands.sql
│     │        ├─ V2__create_users_table.sql
│     │        ├─ V30__create_product_tables.sql
│     │        ├─ V31__add_is_active_to_product_images_suppliers_warehouses.sql
│     │        ├─ V32__align_products_table_with_entity.sql
│     │        ├─ V3__create_roles_table.sql
│     │        ├─ V4__create_user_roles_table.sql
│     │        ├─ V5__create_categories_table.sql
│     │        ├─ V6__create_brands_table.sql
│     │        ├─ V7__create_suppliers_table.sql
│     │        ├─ V8__create_products_table.sql
│     │        └─ V9__create_product_suppliers_table.sql
│     ├─ generated-sources
│     │  └─ annotations
│     ├─ generated-test-sources
│     │  └─ test-annotations
│     ├─ maven-status
│     │  └─ maven-compiler-plugin
│     │     ├─ compile
│     │     │  └─ default-compile
│     │     │     ├─ createdFiles.lst
│     │     │     └─ inputFiles.lst
│     │     └─ testCompile
│     │        └─ default-testCompile
│     │           ├─ createdFiles.lst
│     │           └─ inputFiles.lst
│     └─ test-classes
│        └─ com
│           └─ example
│              └─ auth_system
│                 └─ AuthSystemApplicationTests.class
└─ logs
   ├─ auth-system.log
   ├─ auth-system.log.2026-06-10.0.gz
   ├─ auth-system.log.2026-06-12.0.gz
   ├─ auth-system.log.2026-06-13.0.gz
   ├─ auth-system.log.2026-06-15.0.gz
   ├─ auth-system.log.2026-06-16.0.gz
   ├─ auth-system.log.2026-06-17.0.gz
   ├─ auth-system.log.2026-06-18.0.gz
   └─ auth-system.log.2026-06-19.0.gz

```