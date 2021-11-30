FROM amazoncorretto:11 as builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} student-service.jar
ENTRYPOINT ["java","-jar","/student-service.jar"]
