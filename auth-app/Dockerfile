FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/auth-app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8181
CMD ["java", "-jar", "auth-app.jar"]