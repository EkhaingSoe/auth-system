-- ============================================================
-- V54__update_stock_movements.sql
-- Update stock movements
-- ============================================================

-- Remove old numeric constraint
-- This constraint was used when reference_type was SMALLINT.
ALTER TABLE stock_movements
DROP CONSTRAINT IF EXISTS stock_movements_reference_type_check;


-- Convert reference_type from SMALLINT to VARCHAR
-- Java enum values will now be stored as their names.
ALTER TABLE stock_movements
ALTER COLUMN reference_type TYPE VARCHAR(50)
USING CASE reference_type
    WHEN 0 THEN 'ORDER'
    WHEN 1 THEN 'PURCHASE_ORDER'
    WHEN 2 THEN 'SALES_RETURN'
    WHEN 3 THEN 'SUPPLIER_RETURN'
    WHEN 4 THEN 'STOCK_ADJUSTMENT'
    WHEN 5 THEN 'INVENTORY_COUNT'
    WHEN 6 THEN 'STOCK_TRANSFER'
    WHEN 7 THEN 'INITIAL_STOCK'
    WHEN 8 THEN 'MANUAL'
    ELSE NULL
END;


-- Stock movements are immutable records.
ALTER TABLE stock_movements
DROP COLUMN IF EXISTS updated_at;


-- Index for variant-based stock movement lookups.
CREATE INDEX IF NOT EXISTS idx_stock_movement_variant
ON stock_movements (variant_id);