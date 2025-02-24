FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S  spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx512m", "-Dserver.port=${PORT}",  "-jar", "/app.jar"]
