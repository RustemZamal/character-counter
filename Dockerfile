FROM amazoncorretto:11-alpine-jdk
COPY target/character-calculator-0.0.1-SNAPSHOT.jar server.jar
ENTRYPOINT ["java","-jar", "/server.jar"]