FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/requests-app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8183
CMD ["java", "-jar", "requests-app.jar"]