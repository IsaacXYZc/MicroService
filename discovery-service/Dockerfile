FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/discovery-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8761
CMD ["java", "-jar", "discovery-service-0.0.1-SNAPSHOT.jar"]
