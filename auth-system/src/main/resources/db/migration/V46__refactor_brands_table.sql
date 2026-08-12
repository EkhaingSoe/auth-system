-- Vxx__refactor_brands_table.sql

-- Fix existing NULL values
UPDATE brands
SET is_active = true
WHERE is_active IS NULL;

UPDATE brands
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

UPDATE brands
SET updated_at = CURRENT_TIMESTAMP
WHERE updated_at IS NULL;

-- Enforce NOT NULL
ALTER TABLE brands
ALTER COLUMN is_active SET NOT NULL;

ALTER TABLE brands
ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE brands
ALTER COLUMN updated_at SET NOT NULL;

-- Make name unique if it isn't already
ALTER TABLE brands
ADD CONSTRAINT uk_brands_name UNIQUE (name);