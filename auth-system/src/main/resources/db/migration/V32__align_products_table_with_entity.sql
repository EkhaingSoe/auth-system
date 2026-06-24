-- ============================================================
-- V32__align_products_table_with_entity.sql
-- ============================================================
-- Aligns products table with Product.java entity
-- Drops old columns, adds new columns, modifies data types
-- ============================================================

-- ============================================================
-- 1. DROP columns NOT in Product.java
-- ============================================================

-- These columns exist in V8 but NOT in Product.java
ALTER TABLE products DROP COLUMN IF EXISTS sku CASCADE;
ALTER TABLE products DROP COLUMN IF EXISTS selling_price CASCADE;
ALTER TABLE products DROP COLUMN IF EXISTS cost_price CASCADE;
ALTER TABLE products DROP COLUMN IF EXISTS unit CASCADE;
ALTER TABLE products DROP COLUMN IF EXISTS weight CASCADE;

-- ============================================================
-- 2. ADD columns that ARE in Product.java but missing
-- ============================================================

-- Product type (STOCKABLE, CONSUMABLE, SERVICE)
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS product_type VARCHAR(20) DEFAULT 'STOCKABLE';

-- Sales settings
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS sale_ok BOOLEAN DEFAULT true;
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS pos_ok BOOLEAN DEFAULT true;
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS website_ok BOOLEAN DEFAULT true;

-- Tax rate
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS tax_rate DECIMAL(5,2) DEFAULT 0;

-- ============================================================
-- 3. MODIFY columns to match Product.java
-- ============================================================

-- product_code: VARCHAR(20) → VARCHAR(50)
ALTER TABLE products 
ALTER COLUMN product_code TYPE VARCHAR(50);

-- ============================================================
-- 4. Add constraint for product_type
-- ============================================================

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.constraint_column_usage 
        WHERE constraint_name = 'chk_product_type'
        AND table_name = 'products'
    ) THEN
        ALTER TABLE products ADD CONSTRAINT chk_product_type 
        CHECK (product_type IN ('STOCKABLE', 'CONSUMABLE', 'SERVICE'));
    END IF;
END $$;

-- ============================================================
-- 5. Update existing records with default values
-- ============================================================

UPDATE products SET product_type = 'STOCKABLE' WHERE product_type IS NULL;
UPDATE products SET sale_ok = true WHERE sale_ok IS NULL;
UPDATE products SET pos_ok = true WHERE pos_ok IS NULL;
UPDATE products SET website_ok = true WHERE website_ok IS NULL;
UPDATE products SET tax_rate = 0 WHERE tax_rate IS NULL;

-- ============================================================
-- 6. Make NOT NULL constraints where applicable
-- ============================================================

-- product_type should not be null (default set above)
ALTER TABLE products ALTER COLUMN product_type SET NOT NULL;

-- sale_ok should not be null
ALTER TABLE products ALTER COLUMN sale_ok SET NOT NULL;

-- pos_ok should not be null
ALTER TABLE products ALTER COLUMN pos_ok SET NOT NULL;

-- website_ok should not be null
ALTER TABLE products ALTER COLUMN website_ok SET NOT NULL;

-- tax_rate should not be null
ALTER TABLE products ALTER COLUMN tax_rate SET NOT NULL;

-- ============================================================
-- 7. Add indexes for new columns
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_products_product_type ON products(product_type);
CREATE INDEX IF NOT EXISTS idx_products_sale_ok ON products(sale_ok);

-- ============================================================
-- 8. Add comments for documentation
-- ============================================================

COMMENT ON COLUMN products.product_type IS 'Product type: STOCKABLE, CONSUMABLE, SERVICE';
COMMENT ON COLUMN products.sale_ok IS 'Available for sale';
COMMENT ON COLUMN products.pos_ok IS 'Available in POS';
COMMENT ON COLUMN products.website_ok IS 'Available on website';
COMMENT ON COLUMN products.tax_rate IS 'Tax rate percentage';

-- ============================================================
-- 9. Confirmation
-- ============================================================

DO $$
DECLARE
    col_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO col_count 
    FROM information_schema.columns 
    WHERE table_name = 'products';
    
    RAISE NOTICE '==========================================';
    RAISE NOTICE 'V32 Migration Completed Successfully!';
    RAISE NOTICE '==========================================';
    RAISE NOTICE '✅ Dropped: sku, selling_price, cost_price, unit, weight';
    RAISE NOTICE '✅ Added: product_type, sale_ok, pos_ok, website_ok, tax_rate';
    RAISE NOTICE '✅ Modified: product_code VARCHAR(20) → VARCHAR(50)';
    RAISE NOTICE '✅ Total columns in products table: %', col_count;
    RAISE NOTICE '==========================================';
END $$;