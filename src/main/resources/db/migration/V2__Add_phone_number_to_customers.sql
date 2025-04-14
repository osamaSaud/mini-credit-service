-- Add phone_number column to customers table
ALTER TABLE customers 
ADD COLUMN phone_number VARCHAR(20);

-- Add index to phone_number for faster lookups
CREATE INDEX idx_customer_phone_number ON customers(phone_number); 