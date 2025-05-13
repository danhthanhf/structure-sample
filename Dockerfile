FROM open:jdk-17-slim

WORKDIR app

EXPOSE 8080

COPY target/*.jar app.jar

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
