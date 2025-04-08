# Mini Credit Service

A Spring Boot application for managing customer credit information.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Environment Setup

1. Copy the application properties template:
   ```bash
   cp src/main/resources/application.properties.template src/main/resources/application.properties
   ```

2. Set up environment variables (recommended) or update application.properties directly:
   ```bash
   export MYSQL_USERNAME=your_username
   export MYSQL_PASSWORD=your_password
   ```

   Note: If environment variables are not set, the application will use default values from application.properties.

## Running the Application

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080. You can access the H2 database console at http://localhost:8080/h2-console.

## API Endpoints

The application provides the following REST endpoints:

### Get All Customers
- **URL**: `/api/customers`
- **Method**: GET
- **Response**: List of all customers

### Get Customer by ID
- **URL**: `/api/customers/{id}`
- **Method**: GET
- **Response**: Customer details

### Create Customer
- **URL**: `/api/customers`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "creditScore": 750,
    "annualSalary": 85000.0
  }
  ```
- **Response**: Created customer with credit risk score

### Update Customer
- **URL**: `/api/customers/{id}`
- **Method**: PUT
- **Request Body**: Same as create
- **Response**: Updated customer

### Delete Customer
- **URL**: `/api/customers/{id}`
- **Method**: DELETE
- **Response**: 204 No Content

## Sample Data

The application is pre-populated with 5 sample customers with varying credit scores and salaries:

1. John Doe (Credit Score: 750, Salary: $85,000)
2. Jane Smith (Credit Score: 680, Salary: $65,000)
3. Robert Johnson (Credit Score: 620, Salary: $55,000)
4. Emily Williams (Credit Score: 800, Salary: $120,000)
5. Michael Brown (Credit Score: 550, Salary: $45,000)

## Testing the API

You can use tools like Postman, cURL, or any HTTP client to test the API endpoints. Here are some example cURL commands:

### Get All Customers
```
curl -X GET http://localhost:8080/api/customers
```

### Get Customer by ID
```
curl -X GET http://localhost:8080/api/customers/1
```

### Create Customer
```
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "creditScore": 750,
    "annualSalary": 85000.0
  }'
```

### Update Customer
```
curl -X PUT http://localhost:8080/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "creditScore": 780,
    "annualSalary": 90000.0
  }'
```

### Delete Customer
```
curl -X DELETE http://localhost:8080/api/customers/1
```

## Credit Risk Calculation

The credit risk score is calculated based on:
- Credit score (70% weight)
- Annual salary (30% weight)

The risk score ranges from 0 to 1, where 1 represents the highest risk. 
