FROM bellsoft/liberica-openjdk-alpine:18.0.2-10
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]