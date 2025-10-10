# CursoMaker

### 🧪 Teste a API online em: https://api-comparator.vercel.app/

### 🖥️ Vá para o código do front end: https://github.com/Gustavoksbr/api-comparator

---

## 🧩 Descrição do Projeto

Este projeto é um **sistema de gerenciamento de cursos** desenvolvido em **Java com Spring Boot**. O sistema foi construído seguindo a **arquitetura em três camadas (Controller → Service → Repository)** e integra três tipos diferentes de APIs:
- **REST**
- **GraphQL**
- **SOAP**

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

Crie um banco no **MongoDB** com o nome `cursos_db` ou edite o arquivo `application.properties` com sua URI:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/cursos_db
```

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
* [CursoValidator](./src/main/java/com/example/cursomaker/controller/validator/CursoValidator.java): valida dados de entrada para GRAPHQL e SOAP. Faz a mesma função da biblioteca jakarta.validation usado no REST.

**2. Domain Layer** – Contém as regras de negócio.

* [CursoService](./src/main/java/com/example/cursomaker/dominio/CursoService.java): Onde se isola a lógica de negócio e a comunicação entre diversos serviços. Por ora só se comunica com o repository, mas é um padrão que pode facilitar caso o projeto venha a ter mais serviços (como envio de email, autenticação, etc)
* [Curso](./src/main/java/com/example/cursomaker/dominio/Curso.java) esas model é compartilhada por toda a aplicação e serve de DTO

**3. Repository Layer** – Realiza o acesso ao banco de dados MongoDB.

* [CursoRepository](./src/main/java/com/example/cursomaker/repository/CursoRepository.java): responsável pelas operações no banco de dados

---

## 🌐 Perfis de Execução

Cada tipo de API é ativado por um *profile*:

| Tipo de API | Profile       | Endpoint principal |
| ----------- | ------------- |--------------------|
| REST        | `api-rest`    | `/cursos`          |
| GraphQL     | `api-graphql` | `/graphql`         |
| SOAP        | `api-soap`    | `/ws/cursos`       |

No arquivo `application.properties` você pode escolher quais profiles ativar. Na api hospedada estão todos:

```properties
spring.profiles.active=api-rest,api-graphql,api-soap
```

---


## 🧾 Licença

Este projeto é de uso livre para fins de estudo e demonstração de integração de múltiplos tipos de API com Spring Boot.
