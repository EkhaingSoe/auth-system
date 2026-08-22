-- V56__make_stock_adjustment_difference_not_null.sql

ALTER TABLE stock_adjustments
ALTER COLUMN difference SET NOT NULL;