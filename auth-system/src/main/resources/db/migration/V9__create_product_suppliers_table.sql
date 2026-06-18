-- V9__create_product_suppliers_table.sql
CREATE TABLE IF NOT EXISTS product_suppliers (
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    supplier_id UUID REFERENCES suppliers(id) ON DELETE CASCADE,
    purchase_price DECIMAL(10,2) NOT NULL,
    lead_time_days INTEGER DEFAULT 7,
    is_primary BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, supplier_id)
);