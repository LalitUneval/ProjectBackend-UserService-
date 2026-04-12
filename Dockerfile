# Stage 1: Build stage (Maven + JDK 21)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Copy pom.xml and download dependencies to leverage Docker caching
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage (JRE 21 only)
FROM eclipse-temurin:21-jre-alpine
LABEL authors="Lalit"
WORKDIR /app

# 3. Copy the jar from the build stage
# Matches <artifactId>User-Service</artifactId> from your pom.xml
COPY --from=build /app/target/User-Service-0.0.1-SNAPSHOT.jar app.jar

# Set the port to 8082 as requested
EXPOSE 8082

# 4. Run the application
# We use the --server.port flag to ensure it runs on 8082 in the container
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8082"]