FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/sentimentanalyzer-0.0.2-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]