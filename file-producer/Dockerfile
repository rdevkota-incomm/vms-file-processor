# docker build --build-arg JAR_FILE=build/libs/*.jar -t vms-file-processor .
# docker run -p 8080:8080  vms-file-processor

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} file-producer-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/file-producer-1.0-SNAPSHOT.jar"]
