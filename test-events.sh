#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=======================================${NC}"
echo -e "${YELLOW}   RABBITMQ EVENT TESTING SCRIPT${NC}"
echo -e "${YELLOW}=======================================${NC}"

# Test 1: Send a simple message
echo -e "\n${GREEN}TEST 1: Sending a simple message...${NC}"
curl -s -X POST -H "Content-Type: text/plain" -d "Test message from script" http://localhost:8082/api/messages/simple
echo -e "\n${GREEN}✓ Simple message sent${NC}"

# Test 2: Send a JSON message
echo -e "\n${GREEN}TEST 2: Sending a JSON message...${NC}"
curl -s -X POST -H "Content-Type: application/json" -d '{"message":"JSON test from script"}' http://localhost:8082/api/messages/testJson
echo -e "\n${GREEN}✓ JSON message sent${NC}"

# Test 3: Create a new customer and check for event
echo -e "\n${GREEN}TEST 3: Creating a new customer...${NC}"
CUSTOMER_RESPONSE=$(curl -s -X POST -H "Content-Type: application/json" -d '{
  "firstName": "Test",
  "lastName": "User",
  "email": "test.user@example.com",
  "creditScore": 700,
  "annualSalary": 75000
}' http://localhost:8082/api/customers)

echo "$CUSTOMER_RESPONSE"
CUSTOMER_ID=$(echo "$CUSTOMER_RESPONSE" | grep -o '"id":[0-9]*' | cut -d':' -f2)
echo -e "${GREEN}✓ Customer created with ID: $CUSTOMER_ID${NC}"

# Wait for event processing
sleep 1

# Test 4: Update the customer
echo -e "\n${GREEN}TEST 4: Updating the customer...${NC}"
curl -s -X PUT -H "Content-Type: application/json" -d '{
  "firstName": "Updated",
  "lastName": "User",
  "email": "test.user@example.com",
  "creditScore": 750,
  "annualSalary": 80000
}' http://localhost:8082/api/customers/$CUSTOMER_ID
echo -e "\n${GREEN}✓ Customer updated${NC}"

# Wait for event processing
sleep 1

# Test 5: Delete the customer
echo -e "\n${GREEN}TEST 5: Deleting the customer...${NC}"
curl -s -X DELETE http://localhost:8082/api/customers/$CUSTOMER_ID
echo -e "\n${GREEN}✓ Customer deleted${NC}"

# Final output
echo -e "\n${YELLOW}=======================================${NC}"
echo -e "${YELLOW}   ALL TESTS COMPLETED${NC}"
echo -e "${YELLOW}=======================================${NC}"
echo -e "\n${GREEN}Showing application logs for event processing...${NC}"
docker logs mini-credit-service | grep -E 'CustomerEvent|MessageConsumerService|MessagePublisherService' | tail -n 30
echo -e "\n${GREEN}Check the application logs to verify events were processed:${NC}"
echo "docker logs mini-credit-service | grep -E 'RECEIVED EVENT|Processing CUSTOMER'" 