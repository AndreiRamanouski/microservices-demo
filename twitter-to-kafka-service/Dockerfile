FROM openjdk:11-jre-slim
COPY ./target/*.jar app.jar
ENTRYPOINT java $JAVA_OPTS -jar app.jar