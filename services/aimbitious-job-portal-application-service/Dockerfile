# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module repository
COPY . .

# Make the Application Service Maven wrapper executable
RUN chmod +x services/aimbitious-job-portal-application-service/mvnw

# Build the Application Service and its required Maven modules
RUN ./services/aimbitious-job-portal-application-service/mvnw \
    -f pom.xml \
    -pl services/aimbitious-job-portal-application-service \
    -am \
    -DskipTests \
    package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built Application Service JAR
COPY --from=build /app/services/aimbitious-job-portal-application-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]