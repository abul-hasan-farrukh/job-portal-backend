```dockerfile
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x cloud/aimbitious-job-portal-config-server/mvnw

RUN ./cloud/aimbitious-job-portal-config-server/mvnw \
    -f pom.xml \
    -pl cloud/aimbitious-job-portal-config-server \
    -am \
    -DskipTests \
    package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/cloud/aimbitious-job-portal-config-server/target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
```