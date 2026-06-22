-- V29__add_public_id_to_brands.sql

ALTER TABLE brands ADD COLUMN IF NOT EXISTS public_id VARCHAR(255);

-- Add index for performance (optional)
CREATE INDEX IF NOT EXISTS idx_brands_public_id ON brands(public_id);

-- Verify column was added
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'brands' 
ORDER BY ordinal_position;