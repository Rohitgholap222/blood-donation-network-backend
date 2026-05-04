# Stage 1: Build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first to leverage Docker cache
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Optimization: Run as non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy only the built JAR from the build stage
# Renaming to app.jar for simplicity
COPY --from=build /app/target/smart-blood-network-0.0.1-SNAPSHOT.jar app.jar

# Dynamic port handling
# Spring Boot will automatically use the PORT env var because of server.port=${PORT:8080} in application.properties
ENV PORT=8080
EXPOSE ${PORT}

# Healthcheck for cloud platforms
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
