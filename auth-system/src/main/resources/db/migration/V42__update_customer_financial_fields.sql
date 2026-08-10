-- V42__update_customer_financial_fields.sql

ALTER TABLE customers
    ALTER COLUMN customer_type SET DEFAULT 'WALK_IN';

ALTER TABLE customers
    ALTER COLUMN credit_limit TYPE NUMERIC(19, 2);

ALTER TABLE customers
    ALTER COLUMN current_balance TYPE NUMERIC(19, 2);

ALTER TABLE customers
    ALTER COLUMN credit_limit SET DEFAULT 0;

ALTER TABLE customers
    ALTER COLUMN current_balance SET DEFAULT 0;