FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/orders-0.0.1-SNAPSHOT.jar /app/orders.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app/orders.jar"]
