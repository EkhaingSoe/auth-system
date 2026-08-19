-- V53__add_warehouse_stock_indexes.sql

CREATE INDEX IF NOT EXISTS idx_warehouse_stock_product
    ON warehouse_stocks(product_id);

CREATE INDEX IF NOT EXISTS idx_warehouse_stock_variant
    ON warehouse_stocks(variant_id);

CREATE INDEX IF NOT EXISTS idx_warehouse_stock_warehouse
    ON warehouse_stocks(warehouse_id);