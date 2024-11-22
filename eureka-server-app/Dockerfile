# Build the application with Maven
FROM maven:3.8.4-openjdk-17-slim as build

# Set the working directory for building the app
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Run Maven to build the JAR file (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Stage 2: Run the application in a lightweight OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory for the runtime container
WORKDIR /app

# Copy the JAR from the build stage into the runtime container
COPY --from=build /app/target/*.jar /app/eureka-server-app.jar

# Set the Spring profile to 'docker'
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the port the app runs on
EXPOSE 8761

# Run the application using Java
CMD ["java", "-jar", "eureka-server-app.jar"]
