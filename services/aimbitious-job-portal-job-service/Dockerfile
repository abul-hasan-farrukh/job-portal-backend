# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Maven multi-module project
COPY . .

# Make the root Maven wrapper executable
RUN chmod +x mvnw

# Build the Job Service and required upstream modules
RUN ./mvnw -B -pl services/aimbitious-job-portal-job-service -am -DskipTests clean package

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the executable Job Service JAR
COPY --from=build /app/services/aimbitious-job-portal-job-service/target/*.jar /app/app.jar

# Start the Spring Boot application on Render's assigned port
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-10000} -jar /app/app.jar"]