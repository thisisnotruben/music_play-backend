FROM eclipse-temurin:25-jre

ARG JAR_PATH=target/
ARG JAR_NAME=MusicPlay-0.0.1-SNAPSHOT

COPY ${JAR_PATH}${JAR_NAME}.jar /opt/app.jar

RUN apt-get update && apt-get install -y curl

EXPOSE 8080
ENTRYPOINT java -jar /opt/app.jar