FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/orders-0.0.1-SNAPSHOT.jar"]
