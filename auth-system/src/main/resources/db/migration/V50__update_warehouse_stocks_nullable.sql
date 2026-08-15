ALTER TABLE warehouse_stocks
    ALTER COLUMN current_quantity SET DEFAULT 0,
    ALTER COLUMN current_quantity SET NOT NULL,

    ALTER COLUMN reserved_quantity SET DEFAULT 0,
    ALTER COLUMN reserved_quantity SET NOT NULL,

    ALTER COLUMN min_stock SET DEFAULT 0,
    ALTER COLUMN min_stock SET NOT NULL,

    ALTER COLUMN max_stock SET DEFAULT 0,
    ALTER COLUMN max_stock SET NOT NULL,

    ALTER COLUMN reorder_level SET DEFAULT 0,
    ALTER COLUMN reorder_level SET NOT NULL,

    ALTER COLUMN reorder_quantity SET DEFAULT 0,
    ALTER COLUMN reorder_quantity SET NOT NULL;