FROM eclipse-temurin:25-jre

ARG JAR_PATH=build/libs/
ARG JAR_NAME=MusicPlay-0.0.1-SNAPSHOT

COPY ${JAR_PATH}${JAR_NAME}.jar /opt/app.jar

EXPOSE 8080
ENTRYPOINT java -jar /opt/app.jar  
