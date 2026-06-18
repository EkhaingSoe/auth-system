-- V20__insert_initial_data.sql
-- Insert default store
INSERT INTO stores (name, address, phone) VALUES 
    ('Main Store', '123 Main Street, Yangon', '+959123456789')
ON CONFLICT (store_code) DO NOTHING;

-- Insert default roles
INSERT INTO roles (name) VALUES 
    ('ROLE_ADMIN'),
    ('ROLE_MANAGER'),
    ('ROLE_CASHIER'),
    ('ROLE_CUSTOMER')
ON CONFLICT (name) DO NOTHING;

-- Insert default categories
INSERT INTO categories (name, description) VALUES 
    ('Electronics', 'Electronic devices and accessories'),
    ('Clothing', 'Apparel and fashion items'),
    ('Food & Beverage', 'Food and drink products')
ON CONFLICT (name) DO NOTHING;

-- Insert default brands
INSERT INTO brands (name) VALUES 
    ('Apple'),
    ('Samsung'),
    ('Sony'),
    ('Nike'),
    ('Adidas')
ON CONFLICT (name) DO NOTHING;