# Use the JRE version for a smaller, faster production image
# Amazon Corretto provides multi-arch images by default
FROM amazoncorretto:25.0.2-alpine

# Set the working directory
WORKDIR /app

# Final, safe, explicit copy
# check docker.publish.yaml for creation artefact with fixed name `target/app.jar`
COPY target/app.jar app.jar

# Expose application port
EXPOSE 8080

# Run the JAR file with basic memory optimizations for containers
ENTRYPOINT ["java", "-jar", "app.jar"]