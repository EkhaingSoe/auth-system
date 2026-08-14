-- ============================================================
-- V49: Update product variants
-- ============================================================

-- Fix existing NULL data before adding NOT NULL constraints
UPDATE product_variants
SET currency = 'MMK'
WHERE currency IS NULL;

UPDATE product_variants
SET unit = 'piece'
WHERE unit IS NULL;

UPDATE product_variants
SET is_active = TRUE
WHERE is_active IS NULL;


-- Numeric precision / scale
ALTER TABLE product_variants
    ALTER COLUMN selling_price TYPE NUMERIC(15,2);

ALTER TABLE product_variants
    ALTER COLUMN cost_price TYPE NUMERIC(15,2);

ALTER TABLE product_variants
    ALTER COLUMN weight TYPE NUMERIC(10,3);

ALTER TABLE product_variants
    ALTER COLUMN length TYPE NUMERIC(10,3);

ALTER TABLE product_variants
    ALTER COLUMN width TYPE NUMERIC(10,3);

ALTER TABLE product_variants
    ALTER COLUMN height TYPE NUMERIC(10,3);


-- NOT NULL + defaults
ALTER TABLE product_variants
    ALTER COLUMN currency SET DEFAULT 'MMK',
    ALTER COLUMN currency SET NOT NULL;

ALTER TABLE product_variants
    ALTER COLUMN unit SET DEFAULT 'piece',
    ALTER COLUMN unit SET NOT NULL;

ALTER TABLE product_variants
    ALTER COLUMN is_active SET DEFAULT TRUE,
    ALTER COLUMN is_active SET NOT NULL;


-- Remove stock management from ProductVariant
ALTER TABLE product_variants
    DROP COLUMN IF EXISTS stock_quantity,
    DROP COLUMN IF EXISTS reserved_quantity,
    DROP COLUMN IF EXISTS min_stock_quantity,
    DROP COLUMN IF EXISTS max_stock_quantity,
    DROP COLUMN IF EXISTS reorder_level;


-- Add default variant
ALTER TABLE product_variants
    ADD COLUMN is_default BOOLEAN NOT NULL DEFAULT FALSE;