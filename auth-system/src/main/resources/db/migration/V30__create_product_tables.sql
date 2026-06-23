-- V30__create_product_tables.sql

-- ============================================================
-- 1. Products Table (Master)
-- ============================================================
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id UUID REFERENCES categories(id),
    brand_id UUID REFERENCES brands(id),
    
    -- Product type
    product_type VARCHAR(20) DEFAULT 'STOCKABLE' CHECK (product_type IN ('STOCKABLE', 'CONSUMABLE', 'SERVICE')),
    
    -- Sales settings
    sale_ok BOOLEAN DEFAULT true,
    pos_ok BOOLEAN DEFAULT true,
    website_ok BOOLEAN DEFAULT true,
    
    -- Tax
    tax_rate DECIMAL(5,2) DEFAULT 0,
    
    -- Status
    is_active BOOLEAN DEFAULT true,
    
    -- Audit
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. Product Attributes (Odoo-Style)
-- ============================================================
CREATE TABLE IF NOT EXISTS product_attributes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,  -- Added UNIQUE for conflict handling
    display_name VARCHAR(100),
    attribute_type VARCHAR(30) CHECK (attribute_type IN ('color', 'size', 'material', 'style', 'custom')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 3. Product Attribute Values
-- ============================================================
CREATE TABLE IF NOT EXISTS product_attribute_values (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    attribute_id UUID REFERENCES product_attributes(id) ON DELETE CASCADE,
    value VARCHAR(100) NOT NULL,
    hex_code VARCHAR(7),
    display_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_attribute_value UNIQUE (attribute_id, value)
);

-- ============================================================
-- 4. Product Variants
-- ============================================================
CREATE TABLE IF NOT EXISTS product_variants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    sku VARCHAR(50) UNIQUE NOT NULL,
    barcode VARCHAR(100) UNIQUE,
    
    selling_price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'MMK',
    
    stock_quantity INTEGER DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    min_stock_quantity INTEGER DEFAULT 0,
    max_stock_quantity INTEGER DEFAULT 0,
    reorder_level INTEGER DEFAULT 0,
    
    attribute_values JSONB DEFAULT '{}',
    
    weight DECIMAL(10,2),
    length DECIMAL(10,2),
    width DECIMAL(10,2),
    height DECIMAL(10,2),
    unit VARCHAR(20) DEFAULT 'piece',
    
    is_active BOOLEAN DEFAULT true,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 5. Product Images
-- ============================================================
CREATE TABLE IF NOT EXISTS product_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    variant_id UUID REFERENCES product_variants(id) ON DELETE CASCADE,
    
    image_url VARCHAR(500) NOT NULL,
    public_id VARCHAR(255),
    is_primary BOOLEAN DEFAULT false,
    alt_text VARCHAR(255),
    sort_order INTEGER DEFAULT 0,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT check_product_or_variant CHECK (
        (product_id IS NOT NULL AND variant_id IS NULL) OR
        (product_id IS NULL AND variant_id IS NOT NULL)
    )
);

-- ============================================================
-- 6. Product Suppliers
-- ============================================================
CREATE TABLE IF NOT EXISTS product_suppliers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    supplier_id UUID NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,
    
    supplier_product_code VARCHAR(100),
    supplier_price DECIMAL(10,2),
    lead_time_days INTEGER DEFAULT 7,
    is_primary BOOLEAN DEFAULT false,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT unique_product_supplier UNIQUE (product_id, supplier_id)
);

-- ============================================================
-- 7. Product Warehouses
-- ============================================================
CREATE TABLE IF NOT EXISTS product_warehouses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    warehouse_id UUID REFERENCES stores(id) ON DELETE CASCADE,
    stock_quantity INTEGER DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    min_stock INTEGER DEFAULT 0,
    max_stock INTEGER DEFAULT 0,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT unique_product_warehouse UNIQUE (product_id, warehouse_id)
);

-- ============================================================
-- 8. Indexes
-- ============================================================
-- ============================================================
-- 8. Indexes
-- ============================================================

-- Products
CREATE INDEX IF NOT EXISTS idx_products_name
    ON products(name);

CREATE INDEX IF NOT EXISTS idx_products_category
    ON products(category_id);

CREATE INDEX IF NOT EXISTS idx_products_brand
    ON products(brand_id);

CREATE INDEX IF NOT EXISTS idx_products_code
    ON products(product_code);

CREATE INDEX IF NOT EXISTS idx_products_active
    ON products(is_active);

-- Product Variants
CREATE INDEX IF NOT EXISTS idx_product_variants_product
    ON product_variants(product_id);

CREATE INDEX IF NOT EXISTS idx_product_variants_sku
    ON product_variants(sku);

CREATE INDEX IF NOT EXISTS idx_product_variants_barcode
    ON product_variants(barcode);

-- Product Images
CREATE INDEX IF NOT EXISTS idx_product_images_product
    ON product_images(product_id);

CREATE INDEX IF NOT EXISTS idx_product_images_variant
    ON product_images(variant_id);

-- Product Suppliers
CREATE INDEX IF NOT EXISTS idx_product_suppliers_product
    ON product_suppliers(product_id);

CREATE INDEX IF NOT EXISTS idx_product_suppliers_supplier
    ON product_suppliers(supplier_id);

-- Product Warehouses
CREATE INDEX IF NOT EXISTS idx_product_warehouses_product
    ON product_warehouses(product_id);

CREATE INDEX IF NOT EXISTS idx_product_warehouses_warehouse
    ON product_warehouses(warehouse_id);

-- ============================================================
-- 9. Insert Default Attributes (REMOVED ON CONFLICT)
-- ============================================================
INSERT INTO product_attributes (name, display_name, attribute_type) VALUES 
    ('color', 'Color', 'color'),
    ('size', 'Size', 'size'),
    ('material', 'Material', 'material'),
    ('style', 'Style', 'style');

-- ============================================================
-- 10. Insert Color Values
-- ============================================================
INSERT INTO product_attribute_values (attribute_id, value, hex_code) 
SELECT id, 'Red', '#FF0000' FROM product_attributes WHERE name = 'color'
UNION ALL
SELECT id, 'Blue', '#0000FF' FROM product_attributes WHERE name = 'color'
UNION ALL
SELECT id, 'Black', '#000000' FROM product_attributes WHERE name = 'color'
UNION ALL
SELECT id, 'White', '#FFFFFF' FROM product_attributes WHERE name = 'color';

-- ============================================================
-- 11. Insert Size Values
-- ============================================================
INSERT INTO product_attribute_values (attribute_id, value, display_order) 
SELECT id, 'XS', 1 FROM product_attributes WHERE name = 'size'
UNION ALL
SELECT id, 'S', 2 FROM product_attributes WHERE name = 'size'
UNION ALL
SELECT id, 'M', 3 FROM product_attributes WHERE name = 'size'
UNION ALL
SELECT id, 'L', 4 FROM product_attributes WHERE name = 'size'
UNION ALL
SELECT id, 'XL', 5 FROM product_attributes WHERE name = 'size'
UNION ALL
SELECT id, 'XXL', 6 FROM product_attributes WHERE name = 'size';

-- ============================================================
-- 12. Insert Material Values
-- ============================================================
INSERT INTO product_attribute_values (attribute_id, value) 
SELECT id, 'Cotton' FROM product_attributes WHERE name = 'material'
UNION ALL
SELECT id, 'Polyester' FROM product_attributes WHERE name = 'material'
UNION ALL
SELECT id, 'Leather' FROM product_attributes WHERE name = 'material'
UNION ALL
SELECT id, 'Wool' FROM product_attributes WHERE name = 'material';

-- ============================================================
-- 14. Confirmation
-- ============================================================
DO $$
DECLARE
    default_supplier_id UUID;
    default_supplier_code VARCHAR(20);
    default_supplier_name TEXT; -- ဒီစာကြောင်းကို အသစ်ထည့်ပါ
    attr_count INTEGER;
    val_count INTEGER;
BEGIN
    -- SELECT လုပ်တဲ့နေရာမှာလည်း name ကိုပါ ထည့်ထုတ်ပေးပါ
    SELECT id, supplier_code, name INTO default_supplier_id, default_supplier_code, default_supplier_name
    FROM suppliers
    WHERE name = 'Default Supplier'
    LIMIT 1;

    SELECT COUNT(*) INTO attr_count FROM product_attributes;
    SELECT COUNT(*) INTO val_count FROM product_attribute_values;

    RAISE NOTICE '==========================================';
    RAISE NOTICE 'Product Tables Created Successfully!';
    RAISE NOTICE 'Supplier: % (%)', default_supplier_name, default_supplier_code;
    RAISE NOTICE 'Attributes: %', attr_count;
    RAISE NOTICE 'Attribute Values: %', val_count;
    RAISE NOTICE '==========================================';
END $$