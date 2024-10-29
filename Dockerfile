# Imagen modelo
FROM eclipse-temurin:17.0.13_11-jdk

EXPOSE 8080
WORKDIR /root

COPY target/roomly-0.0.1-SNAPSHOT.jar /root/app.jar

CMD ["java", "-jar", "app.jar"]