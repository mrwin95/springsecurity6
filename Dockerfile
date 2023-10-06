FROM maven:3.8.3-jdk-11 AS build

COPY . /app

WORKDIR /app

RUN mvn package -DskipTests

# second stages
FROM openjdk:11-jre-slim
ENV SPRING_PROFILES_ACTIVE=production
COPY --from=build /app/target/AuthenticatedBackend-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM openjdk:11
#COPY target/AuthenticatedBackend-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]