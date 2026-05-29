# Paso 2: Usar una imagen ligera de Java para ejecutar la aplicación
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el .jar empaquetado desde el paso anterior
COPY /target/*.jar seniamaps.jar

# Exponer el puerto por el que escucha vuestra app de Spring Boot
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "seniamaps.jar"]