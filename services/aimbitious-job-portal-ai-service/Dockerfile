# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module repository
COPY . .

# Make the AI Service Maven wrapper executable
RUN chmod +x services/aimbitious-job-portal-ai-service/mvnw

# Build the AI Service and its required Maven modules
RUN ./services/aimbitious-job-portal-ai-service/mvnw \
    -f pom.xml \
    -pl services/aimbitious-job-portal-ai-service \
    -am \
    -DskipTests \
    package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built AI Service JAR
COPY --from=build /app/services/aimbitious-job-portal-ai-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]