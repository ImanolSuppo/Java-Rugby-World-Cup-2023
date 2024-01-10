FROM eclipse-temurin:17-jdk-alpine
ARG jar_file=target/*.jar
COPY ${jar_file} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]