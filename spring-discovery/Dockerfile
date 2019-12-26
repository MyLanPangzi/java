FROM openjdk:8-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY target/classes .
COPY target/dependency/BOOT-INF/lib lib
COPY target/dependency/META-INF/ META-INF
ENTRYPOINT ["java", "-cp", ".:lib/*", "com.hiscat.springdiscovery.SpringDiscoveryApplication"]

