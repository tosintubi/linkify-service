# Use Amazon Corretto for the JDK
FROM amazoncorretto:17

# Set work directory
WORKDIR /app

# Copy the built JAR file
COPY build/libs/linkify-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Entry point for the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
