-- V28__create_category_images_table.sql


CREATE TABLE IF NOT EXISTS category_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id UUID NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    public_id VARCHAR(255),
    is_primary BOOLEAN DEFAULT false,
    alt_text VARCHAR(255),
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX IF NOT EXISTS idx_category_images_category_id ON category_images(category_id);

-- Unique index to prevent multiple primary images per category
CREATE UNIQUE INDEX IF NOT EXISTS idx_category_primary_image 
ON category_images(category_id) WHERE is_primary = true;

-- ============================================================
-- 3. Verify table was created
-- ============================================================

SELECT table_name, column_name, data_type
FROM information_schema.columns
WHERE table_name = 'category_images'
ORDER BY ordinal_position;