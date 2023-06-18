# imagen base
FROM openjdk:17.0.2
# destino
WORKDIR /app
# origen
COPY ./target/service-courses-0.0.1-SNAPSHOT.jar .
# puerto en la cual se va exponer
EXPOSE 8002
# Indica que al iniciar el contenedor se debe ejecutar el comando "java -jar /service-users-0.0.1-SNAPSHOT.jar"
ENTRYPOINT ["java", "-jar", "service-courses-0.0.1-SNAPSHOT.jar"]


# CONFIGURACION INICIAL PARA GENERAR LA IMAGEN LISTA :D