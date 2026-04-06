# 1. Change the Build Stage to use JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom and source
COPY pom.xml .
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests

# 2. Change the Package Stage to use JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]