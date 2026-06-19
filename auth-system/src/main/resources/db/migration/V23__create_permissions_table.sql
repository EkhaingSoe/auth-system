-- V23__create_permissions_table.sql
CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default permissions
INSERT INTO permissions (name, description) VALUES 
    ('PRODUCT_CREATE', 'Create new products'),
    ('PRODUCT_READ', 'View products'),
    ('PRODUCT_UPDATE', 'Update products'),
    ('PRODUCT_DELETE', 'Delete products'),
    
    ('CATEGORY_CREATE', 'Create categories'),
    ('CATEGORY_READ', 'View categories'),
    ('CATEGORY_UPDATE', 'Update categories'),
    ('CATEGORY_DELETE', 'Delete categories'),
    
    ('BRAND_CREATE', 'Create brands'),
    ('BRAND_READ', 'View brands'),
    ('BRAND_UPDATE', 'Update brands'),
    ('BRAND_DELETE', 'Delete brands'),
    
    ('INVENTORY_VIEW', 'View inventory'),
    ('INVENTORY_MANAGE', 'Manage inventory'),
    
    ('ORDER_CREATE', 'Create orders'),
    ('ORDER_READ', 'View orders'),
    ('ORDER_UPDATE', 'Update orders'),
    ('ORDER_DELETE', 'Delete orders'),
    ('ORDER_REFUND', 'Refund orders'),
    
    ('CUSTOMER_CREATE', 'Create customers'),
    ('CUSTOMER_READ', 'View customers'),
    ('CUSTOMER_UPDATE', 'Update customers'),
    ('CUSTOMER_DELETE', 'Delete customers'),
    
    ('USER_CREATE', 'Create users'),
    ('USER_READ', 'View users'),
    ('USER_UPDATE', 'Update users'),
    ('USER_DELETE', 'Delete users'),
    ('USER_ASSIGN_ROLE', 'Assign roles to users'),
    
    ('STORE_READ', 'View stores'),
    ('STORE_UPDATE', 'Update stores'),
    
    ('REPORT_VIEW_SALES', 'View sales reports'),
    ('REPORT_VIEW_INVENTORY', 'View inventory reports'),
    ('REPORT_EXPORT', 'Export reports'),
    
    ('SETTINGS_VIEW', 'View settings'),
    ('SETTINGS_UPDATE', 'Update settings');