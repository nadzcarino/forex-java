To create docker image:
1. create war file
2.Create Dockerfile in paste the following

    FROM jboss/wildfly
    ADD forex-java.war /opt/jboss/wildfly/standalone/deployments/
    
3. docker build -t filename.war .
4. docker run -it -p 8080:8080 image_name
