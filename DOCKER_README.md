# Docker and Kubernetes Guide for Mini Credit Service

This guide provides instructions for running the Mini Credit Service application using Docker, Docker Compose, and Kubernetes.

## Prerequisites

- Colima (Docker alternative for Mac)
- kubectl (for Kubernetes)
- Docker Hub account

## Docker Setup

### 1. Install Colima (if not already installed)

```bash
brew install colima
```

### 2. Start Colima

```bash
colima start --cpu 2 --memory 4 --disk 10
```

### 3. Build and Run with Docker Compose

```bash
# Build and start the application with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f
```

### 4. Access the Application

The application will be available at: http://localhost:8081

### 5. Stop the Application

```bash
docker-compose down
```

## Publishing to Docker Hub

```bash
# Log in to Docker Hub
docker login --username osamasaud1

# Build the image
docker-compose build

# Push the image to Docker Hub
docker push osamasaud1/mini-credit-service:latest
```

## Kubernetes Deployment

### 1. Start Kubernetes with Colima

```bash
colima start --kubernetes
```

### 2. Apply Kubernetes Configuration

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

### 3. Access the Application

The application will be accessible at: http://localhost:30081

### 4. Delete the Deployment

```bash
kubectl delete namespace credit-service
```

## Troubleshooting

### Docker Issues

- If you encounter permission issues, run the commands with `sudo`.
- If the application doesn't start, check logs with `docker-compose logs -f`.

### Kubernetes Issues

- Check pod status: `kubectl get pods -n credit-service`
- View pod logs: `kubectl logs -n credit-service <pod-name>`
- Describe pod for details: `kubectl describe pod -n credit-service <pod-name>` 