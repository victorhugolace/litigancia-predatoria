FROM maven:3.9-eclipse-temurin-21 AS source
WORKDIR /app
ENV MAVEN_OPTS="-Xmx512m -XX:ActiveProcessorCount=1"
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp dependency:go-offline
COPY src ./src

FROM source AS test
CMD ["mvn", "-B", "-ntp", "verify"]

FROM source AS build
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
