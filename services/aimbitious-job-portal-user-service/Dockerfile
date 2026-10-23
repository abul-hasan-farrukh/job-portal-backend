# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module repository
COPY . .

# Make the Maven wrapper executable
RUN chmod +x services/aimbitious-job-portal-user-service/mvnw

# Build the User Service and its required Maven modules
RUN ./services/aimbitious-job-portal-user-service/mvnw \
    -f pom.xml \
    -pl services/aimbitious-job-portal-user-service \
    -am \
    -DskipTests \
    package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built User Service JAR
COPY --from=build /app/services/aimbitious-job-portal-user-service/target/*.jar app.jar

# Render supplies the PORT environment variable
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]