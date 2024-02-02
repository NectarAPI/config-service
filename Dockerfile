FROM amazoncorretto:17-alpine-jdk
MAINTAINER devs@nectar.software
WORKDIR /etc/config-service
ARG JAR_FILE=build/libs/config-service-3.0.1-alpha.jar
COPY ${JAR_FILE} config-service.jar
COPY keys/* keys/
ENTRYPOINT ["java","-jar","config-service.jar"]
