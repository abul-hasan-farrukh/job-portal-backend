# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module repository
COPY . .

# Make the Notification Service Maven wrapper executable
RUN chmod +x services/aimbitious-job-portal-notification-service/mvnw

# Build the Notification Service and its required Maven modules
RUN ./services/aimbitious-job-portal-notification-service/mvnw \
    -f pom.xml \
    -pl services/aimbitious-job-portal-notification-service \
    -am \
    -DskipTests \
    package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built Notification Service JAR
COPY --from=build /app/services/aimbitious-job-portal-notification-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]