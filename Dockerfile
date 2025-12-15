# =========================
# Stage 1: Build the application using Maven
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies first (for better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests


# =========================
# Stage 2: Run the built JAR with a smaller runtime image
# =========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose your app's port (change if needed)
EXPOSE 5050

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]

