# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module repository
COPY . .

# Make the Job Service Maven wrapper executable
RUN chmod +x services/aimbitious-job-portal-job-service/mvnw

# Build the Job Service and its required Maven modules
RUN ./services/aimbitious-job-portal-job-service/mvnw \
    -f pom.xml \
    -pl services/aimbitious-job-portal-job-service \
    -am \
    -DskipTests \
    package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built Job Service JAR
COPY --from=build /app/services/aimbitious-job-portal-job-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]