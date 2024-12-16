FROM gradle:7-jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

FROM tomcat:9-jdk8

RUN groupadd tomcat
RUN useradd -m -g tomcat tomcat

COPY --from=build /home/gradle/src/build/libs/altoromutual.war /usr/local/tomcat/webapps/patch.war

RUN chown -R tomcat:tomcat /usr/local/tomcat

USER tomcat

EXPOSE 8080
