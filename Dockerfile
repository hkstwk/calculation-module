# Use an OpenJDK 21 base image
FROM amazoncorretto:21 AS build

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/calculation-module.jar calculation-module.jar

# Expose application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "calculation-module.jar"]
