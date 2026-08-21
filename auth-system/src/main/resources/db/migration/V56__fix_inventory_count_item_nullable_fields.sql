-- ============================================================
-- V56__fix_inventory_count_item_nullable_fields.sql
-- ============================================================

-- counted_quantity is NULL until the staff physically counts
-- the item.
ALTER TABLE inventory_count_items
ALTER COLUMN counted_quantity DROP NOT NULL;

-- difference is NULL until counted_quantity is entered.
ALTER TABLE inventory_count_items
ALTER COLUMN difference DROP NOT NULL;