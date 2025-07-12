FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Create non-root user for security
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

USER spring:spring

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]