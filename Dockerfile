FROM openjdk:17-alpine
VOLUME /tmp
ENV DATASOURCE_URL DATASOURCE_URL
ENV DATASOURCE_USERNAME USERNAME
ENV DATASOURCE_PASSWORD PASSWORD
EXPOSE 80
COPY target/*.jar findfood-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/findfood-0.0.1-SNAPSHOT.jar"]
