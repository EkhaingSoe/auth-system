-- ============================================================
-- V55__update_inventory_count_items.sql
-- ============================================================

ALTER TABLE inventory_count_items
ALTER COLUMN inventory_count_id SET NOT NULL;

ALTER TABLE inventory_count_items
ALTER COLUMN product_id SET NOT NULL;

ALTER TABLE inventory_count_items
ALTER COLUMN system_quantity SET NOT NULL;

ALTER TABLE inventory_count_items
ALTER COLUMN counted_quantity SET NOT NULL;

ALTER TABLE inventory_count_items
ALTER COLUMN difference SET NOT NULL;


ALTER TABLE inventory_count_items
ALTER COLUMN notes TYPE TEXT;


-- Stock adjustment relationship
ALTER TABLE inventory_count_items
ADD CONSTRAINT fk_inventory_count_item_stock_adjustment
FOREIGN KEY (stock_adjustment_id)
REFERENCES stock_adjustments(id);


CREATE UNIQUE INDEX IF NOT EXISTS uk_inventory_count_item_stock_adjustment
ON inventory_count_items (stock_adjustment_id);


CREATE INDEX IF NOT EXISTS idx_inventory_count_item_count
ON inventory_count_items (inventory_count_id);

CREATE INDEX IF NOT EXISTS idx_inventory_count_item_product
ON inventory_count_items (product_id);

CREATE INDEX IF NOT EXISTS idx_inventory_count_item_variant
ON inventory_count_items (variant_id);