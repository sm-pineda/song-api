# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Copy the pom.xml (The "Recipe")
COPY pom.xml .

# 2. Copy the src folder (The "Ingredients")
COPY src ./src

# 3. Build the jar file
RUN mvn clean package -DskipTests

# Package stage (The final small image)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]