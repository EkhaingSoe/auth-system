-- V45__improve_suppliers_table.sql

-- ============================================================
-- 1. Update column lengths
-- ============================================================

ALTER TABLE suppliers
    ALTER COLUMN contact_person TYPE VARCHAR(150);

ALTER TABLE suppliers
    ALTER COLUMN phone TYPE VARCHAR(30);

ALTER TABLE suppliers
    ALTER COLUMN email TYPE VARCHAR(150);


-- ============================================================
-- 2. Fix existing NULL timestamps
-- ============================================================

UPDATE suppliers
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

UPDATE suppliers
SET updated_at = CURRENT_TIMESTAMP
WHERE updated_at IS NULL;


-- ============================================================
-- 3. Make timestamps NOT NULL
-- ============================================================

ALTER TABLE suppliers
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE suppliers
    ALTER COLUMN updated_at SET NOT NULL;