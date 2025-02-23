# Use an OpenJDK 21 base image
FROM amazoncorretto:21 AS build

# Set the working directory
WORKDIR /app

# Copy the JAR file (pass the filename dynamically)
ARG JAR_FILE=target/calculation-module-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose application port (if applicable)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]