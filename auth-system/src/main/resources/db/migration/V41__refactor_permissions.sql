-- V31__refactor_permissions.sql

-- Remove existing role-permission assignments first
DELETE FROM role_permissions;

-- Remove existing permissions
DELETE FROM permissions;

-- Brand
INSERT INTO permissions (name, description) VALUES
('BRAND_CREATE', 'Create brands'),
('BRAND_READ', 'View brands'),
('BRAND_UPDATE', 'Update brands'),
('BRAND_DELETE', 'Delete brands');

-- Category
INSERT INTO permissions (name, description) VALUES
('CATEGORY_CREATE', 'Create categories'),
('CATEGORY_READ', 'View categories'),
('CATEGORY_UPDATE', 'Update categories'),
('CATEGORY_DELETE', 'Delete categories');

-- Customer
INSERT INTO permissions (name, description) VALUES
('CUSTOMER_CREATE', 'Create customers'),
('CUSTOMER_READ', 'View customers'),
('CUSTOMER_UPDATE', 'Update customers'),
('CUSTOMER_DELETE', 'Delete customers');

-- Product
INSERT INTO permissions (name, description) VALUES
('PRODUCT_CREATE', 'Create products'),
('PRODUCT_READ', 'View products'),
('PRODUCT_UPDATE', 'Update products'),
('PRODUCT_DELETE', 'Delete products');

-- Store
INSERT INTO permissions (name, description) VALUES
('STORE_CREATE', 'Create stores'),
('STORE_READ', 'View stores'),
('STORE_UPDATE', 'Update stores'),
('STORE_DELETE', 'Delete stores');

-- Supplier
INSERT INTO permissions (name, description) VALUES
('SUPPLIER_CREATE', 'Create suppliers'),
('SUPPLIER_READ', 'View suppliers'),
('SUPPLIER_UPDATE', 'Update suppliers'),
('SUPPLIER_DELETE', 'Delete suppliers');

-- User
INSERT INTO permissions (name, description) VALUES
('USER_CREATE', 'Create users'),
('USER_READ', 'View users'),
('USER_UPDATE', 'Update users'),
('USER_DELETE', 'Delete users'),
('USER_ASSIGN_ROLE', 'Assign roles to users');

-- Inventory
INSERT INTO permissions (name, description) VALUES
('INVENTORY_READ', 'View inventory'),
('INVENTORY_CREATE', 'Create inventory records'),
('INVENTORY_UPDATE', 'Update inventory'),
('INVENTORY_DELETE', 'Delete inventory records'),
('INVENTORY_ADJUST', 'Adjust inventory quantities'),
('INVENTORY_TRANSFER', 'Transfer inventory between warehouses'),
('INVENTORY_COUNT', 'Perform inventory counts');

-- Order
INSERT INTO permissions (name, description) VALUES
('ORDER_CREATE', 'Create orders'),
('ORDER_READ', 'View orders'),
('ORDER_UPDATE', 'Update orders'),
('ORDER_DELETE', 'Delete orders');

-- Payment
INSERT INTO permissions (name, description) VALUES
('PAYMENT_CREATE', 'Create payments'),
('PAYMENT_READ', 'View payments'),
('PAYMENT_UPDATE', 'Update payment records'),
('PAYMENT_REFUND', 'Process payment refunds');

-- Refund
INSERT INTO permissions (name, description) VALUES
('REFUND_CREATE', 'Create refund requests'),
('REFUND_READ', 'View refunds'),
('REFUND_APPROVE', 'Approve refunds');

-- Shipment
INSERT INTO permissions (name, description) VALUES
('SHIPMENT_CREATE', 'Create shipments'),
('SHIPMENT_READ', 'View shipments'),
('SHIPMENT_UPDATE', 'Update shipments'),
('SHIPMENT_DELETE', 'Delete shipments');