FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.hiscat.websocket.redis.Application"]
#ENTRYPOINT ["java", "-jar", "app.jar"]
#普通写法
#ENTRYPOINT ["sh", "-c", "java -jar app.jar ${0} ${@}"]
#命令行写法
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
#环境变量写法