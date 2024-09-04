FROM tomcat:10-jdk17
COPY target/Locparc-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENTRYPOINT ["/bin/bash", "/usr/local/tomcat/bin/catalina.sh", "run"]