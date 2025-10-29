# CursoMaker

### 🧪 Teste a API online em: https://api-comparator.vercel.app/

### 🖥️ Vá para o código do front end: https://github.com/Gustavoksbr/api-comparator

---

## 🧩 Descrição do Projeto

Este projeto é um **sistema de gerenciamento de cursos** desenvolvido em **Java com Spring Boot**. O sistema foi construído seguindo a **arquitetura em três camadas (Controller → Service → Repository)** e integra três tipos diferentes de APIs:
- **REST**
- **GraphQL**
- **SOAP**

A api está hospedada em: https://cursos-api-7vr6.onrender.com, e você pode testá-la em: https://api-comparator.vercel.app/

---

## ⚡ Rodando o projeto localmente

### ⚙️ Requisitos

* **Java 17+**
* **MongoDB**

### 1️⃣ Clonar o projeto

```bash
git clone https://github.com/Gustavoksbr/cursomaker.git
cd cursomaker
```

### 2️⃣ Configurar o banco de dados

Crie um banco no **MongoDB** e edite o arquivo [application.properties](./src/main/resources/application.properties) com sua URI. Por ex:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/cursos_db
```
**obs: caso faça deploy em produção, é recomendável usar variáveis de ambiente

### 3️⃣ Executar o projeto

```bash
mvn spring-boot:run
```

> O projeto subirá na porta **8080** por padrão.

---
## 🚀 Casos de uso

O sistema permite realizar um CRUD com cada um dos tipos de API:

- **Criar** um curso
- **Listar** todos os cursos (ou pelos atributos: código, título, descrição e carga horária)
- **Atualizar** um curso
- **Deletar** um curso

---
## 🏗️ Arquitetura em 3 Camadas

**1. Controller Layer** – Responsável por receber e responder às requisições HTTP, GraphQL ou SOAP.

* [CursoRestController](./src/main/java/com/example/cursomaker/controller/rest/CursoRestController.java): expõe endpoints REST.
* [CursoGraphqlController](./src/main/java/com/example/cursomaker/controller/graphql/CursoGraphqlController.java): define queries e mutations GraphQL conforme o esquema em [schema.graphqls](./src/main/resources/graphql/schema.graphqls)
* [CursoSoapController](./src/main/java/com/example/cursomaker/controller/soap/CursoSoapController.java): define endpoints SOAP conforme o esquema em [cursos.xsd](./src/main/resources/cursos.xsd)

**2. Domain Layer** – Contém as regras de negócio.

* [CursoService](./src/main/java/com/example/cursomaker/domain/CursoService.java): Onde se isola as regras de negócio e se dá a comunicação entre os serviços com lógica pura (agnóstico de bibliotecas externas próprias para tais serviços). Cada método é um caso de uso, utiliza a classe
  [CursoValidator](src/main/java/com/example/cursomaker/domain/CursoValidator.java) e [CursoRepository](./src/main/java/com/example/cursomaker/repository/CursoRepository.java), é chamado pelos controllers e os retorna uma resposta aos controllers
* [Model](./src/main/java/com/example/cursomaker/domain/model) essas models são compartilhadas por toda a aplicação e servem de DTO
* [CursoValidator](src/main/java/com/example/cursomaker/domain/CursoValidator.java): valida os dados de entrada


**3. Repository Layer** – Realiza o acesso ao banco de dados MongoDB.

* [CursoRepository](./src/main/java/com/example/cursomaker/repository/CursoRepository.java): responsável pelas operações no banco de dados


---

## ⚙️ Testes automatizados

Os testes foram projetados para se alinharem à função e responsabilidade principal de cada classe. A complexidade e quantidade de teste aumenta conforme o número de exceções tratadas

####  [CursoRepositoryTestIntegration](./src/test/java/com/example/cursomaker/repository/CursoRepositoryTestIntegration.java)

* **Tipo:** Teste de integração.
* **Objetivo:** Validar o comportamento do repositório e sua comunicação com o MongoDB.
* **Descrição:**

    * Testa a persistência, atualização e remoção de cursos.
    * Verifica lançamentos de exceções específicas do repositório.
    * Por se comunicar diretamente com `CursoMongoRepository` (que estende `MongoRepository`), este teste cobre efetivamente **duas camadas**: `CursoRepository` e `CursoMongoRepository`.

####  [CursoValidatorTestUnitary](./src/test/java/com/example/cursomaker/domain/CursoValidatorTestUnitary.java)

* **Tipo:** Teste unitário.
* **Objetivo:** Verificar a lógica pura de validação dos dados dos cursos.
* **Descrição:**

    * Testa cenários de erro e sucesso para cada campo (código, título, descrição e carga horária).
    * É completamente isolado de outras camadas.
    * Simula diferentes exceções (`ErroDeRequisicaoGeral`) lançadas pelo `CursoValidator`.

#### [CursoServiceTestUnitary](./src/test/java/com/example/cursomaker/domain/CursoServiceTestUnitary.java)

* **Tipo:** Teste unitário.
* **Objetivo:** Garantir o correto funcionamento da orquestração de serviços.
* **Descrição:**

    * A lógica é simples, pois a maior chance de falhas está dentro dos mocks (CursoValidator e CursoRepository)
    * Caso o projeto venha ter mais serviços, este teste unitário se tornará mais valioso.
    * Não vi necessidade de utilizar os asserts do Junit. Só utilizei verify do mockito para garantir que os métodos certos foram chamados dos mocks

#### [CursoRestTestE2E](./src/test/java/com/example/cursomaker/controller/rest/CursoRestTestE2E.java), [CursoGraphqlTestE2E](./src/test/java/com/example/cursomaker/controller/graphql/CursoGraphqlTestE2E.java) e [CursoSoapTestE2E](./src/test/java/com/example/cursomaker/controller/soap/CursoSoapTestE2E.java)

* **Tipo:** Testes ponta a ponta (E2E).
* **Objetivo:** Validar o comportamento completo das APIs REST, GraphQL e SOAP.
* **Descrição:**

    * Utilizam `private WebTestClient webTestClient` (da biblioteca WebFlux) para realizar chamadas reais às rotas configuradas. Essa classe é flexível para os 3 tipos de API.
    * Simulam requisições reais de usuários às APIs e verificam o comportamento do ciclo completo.
    * Permitem verificar tanto o formato da resposta quanto o código de status retornado.


###  Execução dos testes

Primeiro defina a url do MongoDB de testes em [application-test.properties](./src/test/resources/application-test.properties):

```dotenv
spring.data.mongodb.uri=mongodb://localhost:27017/testes_db
```

**obs: é recomendável usar um banco de dados separado para testes, para evitar conflitos com dados reais

Para executar todos os testes:

```bash
mvn test
```

Ou para executar apenas um grupo específico:

```bash
mvn -Dtest=CursoRestTestE2E test
```

---


## 🧾 Licença

Este projeto é de uso livre para fins de estudo e demonstração de integração de múltiplos tipos de API com Spring Boot.
