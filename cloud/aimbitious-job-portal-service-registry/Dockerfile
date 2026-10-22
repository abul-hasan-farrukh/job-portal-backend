FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x cloud/aimbitious-job-portal-service-registry/mvnw

RUN ./cloud/aimbitious-job-portal-service-registry/mvnw -f pom.xml -pl cloud/aimbitious-job-portal-service-registry -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/cloud/aimbitious-job-portal-service-registry/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]