FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY build/libs/filterm3u-0.0.1-SNAPSHOT.jar /opt/app/filterm3u.jar
CMD ["java", "-jar", "/opt/app/filterm3u.jar"]
