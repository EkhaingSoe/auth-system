-- V1__create_stores_table.sql
CREATE TABLE IF NOT EXISTS stores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Auto-generate store code
CREATE SEQUENCE IF NOT EXISTS store_code_seq START 1;

CREATE OR REPLACE FUNCTION generate_store_code()
RETURNS TRIGGER AS $$
BEGIN
    NEW.store_code := 'STR' || LPAD(nextval('store_code_seq')::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER store_code_trigger
BEFORE INSERT ON stores
FOR EACH ROW
EXECUTE FUNCTION generate_store_code();