-- V19__create_audit_logs_table.sql
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    username VARCHAR(50),
    action VARCHAR(50) NOT NULL CHECK (action IN (
        'CREATE_PRODUCT', 'UPDATE_PRODUCT', 'DELETE_PRODUCT',
        'CREATE_ORDER', 'UPDATE_ORDER', 'REFUND_ORDER',
        'CREATE_CUSTOMER', 'UPDATE_CUSTOMER',
        'CREATE_SUPPLIER', 'UPDATE_SUPPLIER',
        'STOCK_IN', 'STOCK_OUT', 'ADJUST_STOCK',
        'CREATE_USER', 'UPDATE_USER', 'DELETE_USER',
        'CREATE_CATEGORY', 'UPDATE_CATEGORY', 'DELETE_CATEGORY',
        'CREATE_BRAND', 'UPDATE_BRAND', 'DELETE_BRAND'
    )),
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID,
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);