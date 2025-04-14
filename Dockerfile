FROM maven:3.8.5-openjdk-11-slim AS build
WORKDIR /app

# Copy the POM file first to leverage Docker cache
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Runtime stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/credit_service_dev2?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=Aa123456
ENV SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING=true

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 