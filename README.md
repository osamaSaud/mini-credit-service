# Mini Credit Service

A Spring Boot application for managing customer credit information and risk assessment.

## Features

- Customer management (CRUD operations)
- Credit risk calculation based on credit score and annual salary
- RESTful API endpoints
- Database integration with MySQL
- Docker and Kubernetes support

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Docker and Kubernetes (optional, for containerization)

## Environment Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/osamaSaud/mini-credit-service.git
   ```

2. Copy the application properties template:
   ```bash
   cp src/main/resources/application.properties.template src/main/resources/application.properties
   ```

3. Set up environment variables (recommended) or update application.properties directly:
   ```bash
   export MYSQL_USERNAME=your_username
   export MYSQL_PASSWORD=your_password
   ```

   Note: If environment variables are not set, the application will use default values from application.properties.

## Running the Application

### Standard Run

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080. You can access the H2 database console at http://localhost:8080/h2-console.

### Docker Setup

1. Install Colima (if not already installed on Mac)
   ```bash
   brew install colima
   ```

2. Start Colima
   ```bash
   colima start --cpu 2 --memory 4 --disk 10
   ```

3. Build and Run with Docker Compose
   ```bash
   docker-compose up -d
   ```

4. Access the Application at http://localhost:8081

5. Stop the Application
   ```bash
   docker-compose down
   ```

### Kubernetes Deployment

1. Start Kubernetes with Colima
   ```bash
   colima start --kubernetes
   ```

2. Apply Kubernetes Configuration
   ```bash
   # Create namespace
   kubectl create namespace credit-service

   # Apply configurations
   kubectl apply -f k8s/mysql-pv.yaml -n credit-service
   kubectl apply -f k8s/mysql-deployment.yaml -n credit-service
   kubectl apply -f k8s/mysql-service.yaml -n credit-service
   kubectl apply -f k8s/app-deployment.yaml -n credit-service
   kubectl apply -f k8s/app-service.yaml -n credit-service

   # Check status
   kubectl get pods -n credit-service
   kubectl get services -n credit-service
   ```

3. Access the Application at http://localhost:30081

4. Delete the Deployment
   ```bash
   kubectl delete namespace credit-service
   ```

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

## Credit Risk Calculation

The credit risk score is calculated based on:
- Credit score (70% weight)
- Annual salary (30% weight)

The risk score ranges from 0 to 1, where 1 represents the highest risk.

## Troubleshooting

### Docker Issues
- If you encounter permission issues, run the commands with `sudo`
- If the application doesn't start, check logs with `docker-compose logs -f`

### Kubernetes Issues
- Check pod status: `kubectl get pods -n credit-service`
- View pod logs: `kubectl logs -n credit-service <pod-name>`
- Describe pod for details: `kubectl describe pod -n credit-service <pod-name>`
