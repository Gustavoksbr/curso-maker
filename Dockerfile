# Etapa 1: Build da aplicação
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml e baixa dependências (para aproveitar cache)
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copia o restante do projeto
COPY src ./src

# Executa o build do JAR sem rodar testes
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Define o comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
