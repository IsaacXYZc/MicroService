FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY ./gradlew .
COPY ./build.gradle .
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon
EXPOSE 8761
CMD ["java", "-jar", "build/libs/discovery-service-0.0.1-SNAPSHOT.jar"]
