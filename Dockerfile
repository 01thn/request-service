FROM openjdk:21

WORKDIR /app

COPY target/request-service-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "request-service-0.0.1-SNAPSHOT.jar"]