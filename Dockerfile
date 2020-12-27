FROM gradle:6.7.1-jdk8-hotspot

WORKDIR ./
COPY ./ ./
RUN gradle build

FROM jboss/wildfly:latest

RUN /opt/jboss/wildfly/bin/add-user.sh admin 0 --silent
ADD ./build/libs/app.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh","-c","standalone-full-ha.xml","-b","0.0.0.0","-bmanagement", "0.0.0.0"]