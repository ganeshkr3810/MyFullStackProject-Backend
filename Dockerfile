# Use OpenJDK 21 base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file from target folder into container
COPY target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 5050

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
