FROM openjdk:8-jdk-alpine
COPY *.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["sh", "-c", "java -jar app.jar ${0} ${@}"]
