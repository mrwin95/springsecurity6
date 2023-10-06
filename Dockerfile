FROM maven:3.8.3-jdk-11 AS build

COPY . /app

WORKDIR /app

RUN mvn package -DskipTests

# second stages
FROM openjdk:11-jre-slim
ENV SPRING_PROFILES_ACTIVE=production
COPY --from=build /app/target/AuthenticatedBackend-0.0.1-SNAPSHOT.jar /app.jar
HEALTHCHECK --interval=5s \
            --timeout=3s \
            CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM openjdk:11 as builder
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
#COPY src/ ./src/
#RUN mvn package -DskipTests
#
#FROM openjdk:11
#COPY --from=builder /app/target/AuthenticatedBackend-0.0.1-SNAPSHOT.jar /app.jar
#ENTRYPOINT["java", "-jar", "/app.jar"]

#FROM openjdk:11
#COPY target/AuthenticatedBackend-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]