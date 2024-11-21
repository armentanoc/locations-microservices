FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/users-app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8184
CMD ["java", "-jar", "users-app.jar"]