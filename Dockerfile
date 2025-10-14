# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies first (to use cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy all source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the built JAR with a smaller image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 5050

ENTRYPOINT ["java", "-jar", "app.jar"]
