
FROM openjdk:17

WORKDIR /app

COPY target/spaceRoulette.jar /app/

CMD ["java", "-jar", "spaceRoulette.jar"]