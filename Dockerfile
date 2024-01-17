FROM openjdk:17

ARG JAR_PATH=build/libs/fullcar-0.0.1-SNAPSHOT.jar

COPY ${JAR_PATH} fullcar.jar

ENTRYPOINT ["java","-jar","/fullcar.jar"]