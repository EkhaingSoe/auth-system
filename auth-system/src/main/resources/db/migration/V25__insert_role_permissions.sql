-- V25__insert_role_permissions.sql

-- ===== ADMIN =====
-- Admin gets ALL permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
    id 
FROM permissions;

-- ===== MANAGER =====
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'ROLE_MANAGER'),
    id 
FROM permissions 
WHERE name IN (
    'PRODUCT_READ', 'PRODUCT_CREATE', 'PRODUCT_UPDATE',
    'CATEGORY_READ', 'CATEGORY_CREATE', 'CATEGORY_UPDATE',
    'BRAND_READ', 'BRAND_CREATE', 'BRAND_UPDATE',
    'INVENTORY_VIEW', 'INVENTORY_MANAGE',
    'ORDER_READ',
    'CUSTOMER_READ', 'CUSTOMER_CREATE', 'CUSTOMER_UPDATE',
    'STORE_READ',
    'REPORT_VIEW_SALES', 'REPORT_VIEW_INVENTORY', 'REPORT_EXPORT'
);

-- ===== CASHIER =====
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'ROLE_CASHIER'),
    id 
FROM permissions 
WHERE name IN (
    'PRODUCT_READ',
    'CATEGORY_READ',
    'BRAND_READ',
    'INVENTORY_VIEW',
    'ORDER_CREATE', 'ORDER_READ',
    'CUSTOMER_READ', 'CUSTOMER_CREATE',
    'STORE_READ'
);

