-- ============================================================
-- V31__add_is_active_to_product_images_suppliers_warehouses.sql
-- ============================================================

-- Add is_active column with default value true
ALTER TABLE product_images 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT true;

-- Update existing records
UPDATE product_images 
SET is_active = true 
WHERE is_active IS NULL;

-- Make NOT NULL
ALTER TABLE product_images 
ALTER COLUMN is_active SET NOT NULL;

-- Add index for performance
CREATE INDEX IF NOT EXISTS idx_product_images_is_active 
ON product_images(is_active);

-- Add comment for documentation
COMMENT ON COLUMN product_images.is_active 
IS 'Soft delete flag - false means image is deactivated/removed';

-- ============================================================
-- 2. SUPPLIERS
-- ============================================================

-- Add is_active column with default value true
ALTER TABLE suppliers 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT true;

-- Update existing records
UPDATE suppliers 
SET is_active = true 
WHERE is_active IS NULL;

-- Make NOT NULL
ALTER TABLE suppliers 
ALTER COLUMN is_active SET NOT NULL;

-- Add index for performance
CREATE INDEX IF NOT EXISTS idx_suppliers_is_active 
ON suppliers(is_active);

-- Add comment for documentation
COMMENT ON COLUMN suppliers.is_active 
IS 'Soft delete flag - false means supplier is deactivated';

-- ============================================================
-- 3. WAREHOUSES (if table exists)
-- ============================================================

-- Check if warehouses table exists
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM information_schema.tables 
        WHERE table_name = 'warehouses'
    ) THEN
        -- Add is_active column
        ALTER TABLE warehouses 
        ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT true;

        -- Update existing records
        UPDATE warehouses 
        SET is_active = true 
        WHERE is_active IS NULL;

        -- Make NOT NULL
        ALTER TABLE warehouses 
        ALTER COLUMN is_active SET NOT NULL;

        -- Add index
        CREATE INDEX IF NOT EXISTS idx_warehouses_is_active 
        ON warehouses(is_active);

        -- Add comment
        COMMENT ON COLUMN warehouses.is_active 
        IS 'Soft delete flag - false means warehouse is deactivated';
        
        RAISE NOTICE '✅ Added is_active to warehouses';
    ELSE
        RAISE NOTICE '⚠️ warehouses table does not exist - skipping';
    END IF;
END $$;

-- ============================================================
-- Confirmation
-- ============================================================

DO $$
BEGIN
    RAISE NOTICE '==========================================';
    RAISE NOTICE 'V31 Migration Completed Successfully!';
    RAISE NOTICE '==========================================';
    RAISE NOTICE '✅ product_images - is_active added';
    RAISE NOTICE '✅ suppliers - is_active added';
    RAISE NOTICE '✅ warehouses - is_active added (if exists)';
    RAISE NOTICE '==========================================';
END $$;