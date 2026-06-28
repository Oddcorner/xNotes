# Dockerfile (Same file structure for all three services)

# Step 1: Build stage using Maven and Java 25
FROM maven:3.9.16-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies before copying source code
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Ultra-lean run stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
# Copy the compiled .jar file from the build stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
