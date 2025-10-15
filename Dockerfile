# Etapa 1: Build e Teste
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia apenas o pom.xml para aproveitar cache de dependências
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copia o restante do projeto
COPY src ./src

# Define o ARG para injetar a URI de testes
ARG MONGODB_URI_TESTS

# Torna o ARG disponível como variável de ambiente temporária para o Maven
ENV MONGODB_URI_TESTS=${MONGODB_URI_TESTS}

# Executa os testes automatizados
RUN echo "Usando MONGODB_URI_TESTS=${MONGODB_URI_TESTS}" && mvn clean test

# Depois do sucesso dos testes, gera o jar final (sem reexecutar testes)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o jar gerado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
