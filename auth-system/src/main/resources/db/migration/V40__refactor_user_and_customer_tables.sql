-- ============================
-- USER TABLE
-- ============================

ALTER TABLE users
ADD COLUMN user_type VARCHAR(20) NOT NULL DEFAULT 'STAFF';


-- ============================
-- CUSTOMER TABLE
-- ============================

ALTER TABLE customers
ADD COLUMN customer_type VARCHAR(20) NOT NULL DEFAULT 'WALK_IN';

ALTER TABLE customers
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE customers
ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE customers
ADD COLUMN credit_limit NUMERIC(15,2) DEFAULT 0;

ALTER TABLE customers
ADD COLUMN current_balance NUMERIC(15,2) DEFAULT 0;


-- Phone unique constraint
ALTER TABLE customers
ADD CONSTRAINT uk_customer_phone UNIQUE(phone);