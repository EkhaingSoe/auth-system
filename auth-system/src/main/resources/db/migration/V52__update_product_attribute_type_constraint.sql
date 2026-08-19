ALTER TABLE product_attributes
DROP CONSTRAINT IF EXISTS product_attributes_attribute_type_check;

UPDATE product_attributes
SET attribute_type = UPPER(attribute_type);

ALTER TABLE product_attributes
ADD CONSTRAINT product_attributes_attribute_type_check
CHECK (
    attribute_type IN (
        'COLOR',
        'SIZE',
        'STORAGE',
        'MATERIAL',
        'STYLE',
        'CUSTOM'
    )
);