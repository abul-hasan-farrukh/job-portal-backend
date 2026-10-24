# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module repository
COPY . .

# Make the API Gateway Maven wrapper executable
RUN chmod +x cloud/aimbitious-job-portal-api-gateway/mvnw

# Build the API Gateway and its required Maven modules
RUN ./cloud/aimbitious-job-portal-api-gateway/mvnw \
    -f pom.xml \
    -pl cloud/aimbitious-job-portal-api-gateway \
    -am \
    -DskipTests \
    package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built API Gateway JAR
COPY --from=build /app/cloud/aimbitious-job-portal-api-gateway/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]