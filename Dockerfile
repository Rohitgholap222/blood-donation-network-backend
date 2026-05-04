# Stage 1: Build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first to leverage Docker cache
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime
# Using eclipse-temurin as openjdk:17-jdk-slim is deprecated
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Install curl for healthcheck and clean up
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Optimization: Run as non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy only the built JAR from the build stage
COPY --from=build /app/target/smart-blood-network-0.0.1-SNAPSHOT.jar app.jar

# Dynamic port handling (Render will provide PORT)
ENV PORT=8080
EXPOSE ${PORT}

# Healthcheck for cloud platforms
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
