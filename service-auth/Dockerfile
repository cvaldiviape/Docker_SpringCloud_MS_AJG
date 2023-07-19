# imagen base
FROM openjdk:17.0.2
# destino
WORKDIR /app
# origen
COPY ./target/service-auth-0.0.1-SNAPSHOT.jar .
# estableciendo variables de entorno, en "PORT" esta siendo usada en el archivo "application.properties", es decir, es el puerto interno de mi aplicacion
ENV PORT 9000
# puerto en la cual se va exponer (cambie "9000" por "$PORT" para que tome de la variable de entorno que estoy configurando, ya que en esta ocasion el puesto interno y externo son iguales)
EXPOSE $PORT
#EXPOSE 9000

# Indica que al iniciar el contenedor se debe ejecutar el comando "java -jar /service-auth-0.0.1-SNAPSHOT.jar"
ENTRYPOINT ["java", "-jar", "service-auth-0.0.1-SNAPSHOT.jar"]


# CONFIGURACION INICIAL PARA GENERAR LA IMAGEN LISTA :D