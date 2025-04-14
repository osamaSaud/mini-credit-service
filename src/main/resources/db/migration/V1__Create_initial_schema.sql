-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    credit_score INT,
    annual_salary DOUBLE NOT NULL,
    credit_risk_score DOUBLE
);

-- Create salary_certificates table
CREATE TABLE IF NOT EXISTS salary_certificates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    basic_wage VARCHAR(255),
    housing_allowance VARCHAR(255),
    other_allowance VARCHAR(255),
    full_wage VARCHAR(255),
    employer_name VARCHAR(255),
    date_of_joining VARCHAR(255),
    working_months VARCHAR(255),
    employment_status VARCHAR(255),
    salary_starting_date VARCHAR(255),
    establishment_activity VARCHAR(255),
    commercial_registration_number VARCHAR(255),
    legal_entity VARCHAR(255),
    date_of_birth VARCHAR(255),
    nationality VARCHAR(255),
    gosinumber VARCHAR(255),
    created_at DATETIME,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
); 