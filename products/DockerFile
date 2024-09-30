FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY ./gradlew .
COPY ./build.gradle .
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/products-0.0.1-SNAPSHOT.jar"]
