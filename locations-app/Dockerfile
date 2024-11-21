FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/locations-app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8182
CMD ["java", "-jar", "locations-app.jar"]