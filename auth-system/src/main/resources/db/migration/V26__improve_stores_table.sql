-- V25__improve_stores_table.sql

-- ============================================================
-- 1. Add New Columns
-- ============================================================

-- Add store type
ALTER TABLE stores ADD COLUMN store_type VARCHAR(20) DEFAULT 'BRANCH';
ALTER TABLE stores ADD CONSTRAINT chk_store_type 
    CHECK (store_type IN ('HEAD_OFFICE', 'BRANCH', 'WAREHOUSE'));

-- Add parent store (for hierarchy)
ALTER TABLE stores ADD COLUMN parent_store_id UUID REFERENCES stores(id);

-- Add status (replaces is_active)
ALTER TABLE stores ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';
ALTER TABLE stores ADD CONSTRAINT chk_store_status 
    CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'CLOSED'));

-- Add settings (JSONB for flexible configuration)
ALTER TABLE stores ADD COLUMN settings JSONB DEFAULT '{}';

-- Add contact person
ALTER TABLE stores ADD COLUMN contact_person VARCHAR(255);

-- Add tax registration number
ALTER TABLE stores ADD COLUMN tax_number VARCHAR(50);

-- ============================================================
-- 2. Migrate Data from is_active to status
-- ============================================================

UPDATE stores 
SET status = CASE 
    WHEN is_active = true THEN 'ACTIVE'
    ELSE 'INACTIVE'
END;

-- ============================================================
-- 3. Drop is_active column (after migration)
-- ============================================================

ALTER TABLE stores DROP COLUMN is_active;

-- ============================================================
-- 4. Create Indexes
-- ============================================================

CREATE INDEX idx_stores_store_code ON stores(store_code);
CREATE INDEX idx_stores_status ON stores(status);
CREATE INDEX idx_stores_type ON stores(store_type);
CREATE INDEX idx_stores_parent ON stores(parent_store_id);
CREATE INDEX idx_stores_name ON stores(name);

-- ============================================================
-- 5. Update Timestamp Trigger (Reusable Function)
-- ============================================================

-- Create reusable function for all tables
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Drop existing trigger if any
DROP TRIGGER IF EXISTS update_store_timestamp ON stores;

-- Create trigger using reusable function
CREATE TRIGGER update_store_timestamp
BEFORE UPDATE ON stores
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- ============================================================
-- 6. Update Store Code Generation
-- ============================================================

-- Drop existing trigger
DROP TRIGGER IF EXISTS store_code_trigger ON stores;

-- Create improved function (handles null store_code)
CREATE OR REPLACE FUNCTION generate_store_code()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.store_code IS NULL THEN
        NEW.store_code := 'STR' || LPAD(nextval('store_code_seq')::TEXT, 4, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Recreate trigger
CREATE TRIGGER store_code_trigger
BEFORE INSERT ON stores
FOR EACH ROW
EXECUTE FUNCTION generate_store_code();

-- ============================================================
-- 7. Sample Store Data (Improved - No Duplicates)
-- ============================================================

-- Insert Head Office (only if none exists)
INSERT INTO stores (name, address, phone, email, store_type, status) 
SELECT 
    'Head Office', 
    '123 Main Street, Yangon', 
    '+959123456789', 
    'ho@pos.com', 
    'HEAD_OFFICE', 
    'ACTIVE'
WHERE NOT EXISTS (
    SELECT 1 FROM stores WHERE store_type = 'HEAD_OFFICE'
);

-- Insert Branches (only if Head Office exists)
INSERT INTO stores (name, address, phone, email, store_type, status, parent_store_id) 
SELECT 
    'Store 1 - Yangon', 
    '456 Market Road, Yangon', 
    '+959987654321', 
    'store1@pos.com', 
    'BRANCH', 
    'ACTIVE',
    (SELECT id FROM stores WHERE store_type = 'HEAD_OFFICE' LIMIT 1)
WHERE EXISTS (SELECT 1 FROM stores WHERE store_type = 'HEAD_OFFICE')
AND NOT EXISTS (SELECT 1 FROM stores WHERE name = 'Store 1 - Yangon');

INSERT INTO stores (name, address, phone, email, store_type, status, parent_store_id) 
SELECT 
    'Store 2 - Mandalay', 
    '789 Mandalay Road, Mandalay', 
    '+959456789012', 
    'store2@pos.com', 
    'BRANCH', 
    'ACTIVE',
    (SELECT id FROM stores WHERE store_type = 'HEAD_OFFICE' LIMIT 1)
WHERE EXISTS (SELECT 1 FROM stores WHERE store_type = 'HEAD_OFFICE')
AND NOT EXISTS (SELECT 1 FROM stores WHERE name = 'Store 2 - Mandalay');

INSERT INTO stores (name, address, phone, email, store_type, status, parent_store_id) 
SELECT 
    'Central Warehouse', 
    'Industrial Zone, Yangon', 
    '+959111222333', 
    'warehouse@pos.com', 
    'WAREHOUSE', 
    'ACTIVE',
    (SELECT id FROM stores WHERE store_type = 'HEAD_OFFICE' LIMIT 1)
WHERE EXISTS (SELECT 1 FROM stores WHERE store_type = 'HEAD_OFFICE')
AND NOT EXISTS (SELECT 1 FROM stores WHERE name = 'Central Warehouse');