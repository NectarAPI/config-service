FROM adoptopenjdk/openjdk13:alpine-jre
MAINTAINER reagan@nectar.software
WORKDIR /etc/config-service
ARG JAR_FILE=build/libs/config-service-1.11.0-alpha.jar
COPY ${JAR_FILE} config-service.jar
COPY keys/* keys/
ENTRYPOINT ["java","-jar","config-service.jar"]
