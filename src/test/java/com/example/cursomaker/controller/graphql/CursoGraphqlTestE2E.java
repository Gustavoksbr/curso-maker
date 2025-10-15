package com.example.cursomaker.controller.graphql;

import com.example.cursomaker.repository.CursoMongoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CursoGraphqlTestE2E {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;
    @Autowired
    private CursoMongoRepository cursoMongoRepository;
    @BeforeEach
    void setupClient() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/graphql")
                .build();
    }

    @AfterAll
    void limparTudo(){
        cursoMongoRepository.deleteAll();
    }
    @Test
    @Order(2)
    void deveCriarCursoComSucesso() {
        var query = """
        mutation {
            createCurso(
                codigo: "CURSO123",
                titulo: "Spring Boot Completo",
                descricao: "Aprenda Spring Boot do zero",
                cargaHoraria: 40
            ) {
                codigo
                titulo
                descricao
                cargaHoraria
            }
        }
        """;

        var body = Map.of("query", query);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.createCurso.codigo").isEqualTo("CURSO123")
                .jsonPath("$.data.createCurso.titulo").isEqualTo("Spring Boot Completo")
                .jsonPath("$.data.createCurso.descricao").isEqualTo("Aprenda Spring Boot do zero")
                .jsonPath("$.data.createCurso.cargaHoraria").isEqualTo(40);
    }

    @Test
    @Order(3)
    void deveLancarErroAoCriarCursoComCodigoExistente() {
        var mutation = """
    mutation {
        createCurso(
            codigo: "CURSO123",
            titulo: "Curso Duplicado",
            descricao: "Descrição",
            cargaHoraria: 20
        ) {
            codigo
        }
    }
    """;

        var body = Map.of("query", mutation);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").exists();
    }
    @Test
    @Order(2)
    void deveBuscarCursoPorCodigoComSucesso() {
        var query = """
        query {
            getCursoByCodigo(codigo: "CURSO123") {
                codigo
                titulo
                cargaHoraria
            }
        }
        """;

        var body = Map.of("query", query);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.getCursoByCodigo.codigo").isEqualTo("CURSO123")
                .jsonPath("$.data.getCursoByCodigo.titulo").isEqualTo("Spring Boot Completo")
                .jsonPath("$.data.getCursoByCodigo.descricao").doesNotExist()
                .jsonPath("$.data.getCursoByCodigo.cargaHoraria").isEqualTo(40);
    }

    @Test
    @Order(4)
    void deveBuscarCursoPorDescricaoComSucesso() {
        var query = """
        query {
            listCursos(descricao: "Spring Boot") {
                codigo
                descricao
            }
        }
        """;

        var body = Map.of("query", query);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.listCursos[0].codigo").isEqualTo("CURSO123")
                .jsonPath("$.data.listCursos[0].descricao").isEqualTo("Aprenda Spring Boot do zero")
                .jsonPath("$.data.listCursos.length()").isEqualTo(1)
                .jsonPath("$.data.listCursos[0].titulo").doesNotExist()
                .jsonPath("$.data.listCursos[0].cargaHoraria").doesNotExist()
        ;

    }

    @Test
    @Order(5)
    void deveAtualizarCursoComSucesso() {
        var query = """
        mutation {
            updateCurso(
                codigo: "CURSO123",
                codigoNovo: "CURSO456",
                titulo: "Spring Boot Avançado",
                descricao: "Curso atualizado",
                cargaHoraria: 60
            ) {
                codigo
                titulo
            }
        }
        """;

        var body = Map.of("query", query);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("\"codigo\":\"CURSO456\"")));
    }

    @Test
    @Order(6)
    void deveListarTodosOsCursosComSucesso(){
        var query = """
        query {
            listCursos {
                codigo
                titulo
                descricao
                cargaHoraria
            }
        }
        """;

        var body = Map.of("query", query);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.listCursos[0].codigo").isEqualTo("CURSO456")
                .jsonPath("$.data.listCursos[0].titulo").isEqualTo("Spring Boot Avançado")
                .jsonPath("$.data.listCursos[0].descricao").isEqualTo("Curso atualizado")
                .jsonPath("$.data.listCursos[0].cargaHoraria").isEqualTo(60)
                .jsonPath("$.data.listCursos.length()").isEqualTo(1);
    }

    @Test
    @Order(7)
    void deveExcluirCursoComSucesso() {
        var delete = """
        mutation {
            deleteCurso(codigo: "CURSO456")
        }
        """;

        var body = Map.of("query", delete);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.deleteCurso").isEqualTo(true);

    }
    @Test
    void deveLancarErroAoBuscarCursoInexistente() {
        var query = """
    query {
        getCursoByCodigo(codigo: "CURSO999") {
            codigo
        }
    }
    """;

        var body = Map.of("query", query);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").exists();
    }

    @Test
    void deveLancarErroAoAtualizarCursoInexistente() {
        var mutation = """
    mutation {
        updateCurso(
            codigo: "CURSO999",
            codigoNovo: "CURSO999",
            titulo: "Curso Inexistente",
            descricao: "Descrição",
            cargaHoraria: 10
        ) {
            codigo
        }
    }
    """;

        var body = Map.of("query", mutation);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").exists();
    }

    @Test
    void deveLancarErroAoDeletarCursoInexistente() {
        var mutation = """
    mutation {
        deleteCurso(codigo: "CURSO999")
    }
    """;

        var body = Map.of("query", mutation);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").exists();
    }



    @Test
    void deveLancarErroAoCriarCursoComDadosInvalidos() {
        var mutation = """
    mutation {
        createCurso(
            codigo: "",
            titulo: "Curso inválido",
            descricao: "Este curso é inválido pq o código está vazio",
            cargaHoraria: 30
        ) {
            codigo
        }
    }
    """;

        var body = Map.of("query", mutation);

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").exists();
    }
}
