# docker build --build-arg JAR_FILE=build/libs/*.jar -t vms-file-consumer .
# docker run -p 8080:8080  --network="host" vms-file-consumer

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} return-file-consumer-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/return-file-consumer-1.0-SNAPSHOT.jar"]
