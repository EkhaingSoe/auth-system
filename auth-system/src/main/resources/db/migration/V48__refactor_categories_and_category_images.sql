-- V48__refactor_categories_and_category_images.sql

-- ============================================================
-- 1. Prepare existing category data
-- ============================================================

-- Generate slug for existing categories where slug is NULL.
UPDATE categories
SET slug = LOWER(
    REGEXP_REPLACE(
        TRIM(name),
        '[^a-zA-Z0-9]+',
        '-',
        'g'
    )
)
WHERE slug IS NULL;


-- Remove leading/trailing '-' from generated slugs
UPDATE categories
SET slug = TRIM(BOTH '-' FROM slug)
WHERE slug IS NOT NULL;


-- ============================================================
-- 2. Prepare existing category default values
-- ============================================================

UPDATE categories
SET is_active = TRUE
WHERE is_active IS NULL;

UPDATE categories
SET sort_order = 0
WHERE sort_order IS NULL;


-- ============================================================
-- 3. Refactor categories table
-- ============================================================

ALTER TABLE categories
    ALTER COLUMN slug TYPE VARCHAR(150),
    ALTER COLUMN slug SET NOT NULL,
    ALTER COLUMN is_active SET NOT NULL,
    ALTER COLUMN sort_order SET NOT NULL,
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN updated_at SET NOT NULL;


-- ============================================================
-- 4. Refactor category_images table
-- ============================================================

UPDATE category_images
SET is_primary = FALSE
WHERE is_primary IS NULL;

UPDATE category_images
SET sort_order = 0
WHERE sort_order IS NULL;


ALTER TABLE category_images
    ALTER COLUMN image_url TYPE VARCHAR(500),
    ALTER COLUMN image_url SET NOT NULL,
    ALTER COLUMN public_id TYPE VARCHAR(255),
    ALTER COLUMN alt_text TYPE VARCHAR(255),
    ALTER COLUMN is_primary SET NOT NULL,
    ALTER COLUMN sort_order SET NOT NULL,
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN updated_at SET NOT NULL;