FROM gradle:6.7.1-jdk8-hotspot

WORKDIR ./
COPY ./ ./
RUN gradle build

FROM tomcat:9

ADD ./build/libs/app.war /usr/local/tomcat/webapps/

EXPOSE 8080 8005

CMD ["catalina.sh","run"]