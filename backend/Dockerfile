FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/smartcatalog-0.0.1-SNAPSHOT.jar smartcatalog.jar
ENTRYPOINT ["java","-jar","/smartcatalog.jar"]
