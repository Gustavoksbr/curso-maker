package com.example.cursomaker.controller.rest;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CursoRestTestE2E {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setupClient() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:"+port+"/cursos")
                .build();
    }

    // ------------------------------------------------------
    @Test
    @Order(1)
    void deveCriarCursoComSucesso() {
        var request = """
            {
              "codigo": "CURSO123",
              "titulo": "Spring Boot Completo",
              "descricao": "Aprenda Spring Boot do zero",
              "cargaHoraria": 40
            }
        """;

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.codigo").isEqualTo("CURSO123")
                .jsonPath("$.titulo").isEqualTo("Spring Boot Completo");
    }

    // ------------------------------------------------------

    @Test
    @Order(2)
    void deveLancarErroAoCriarCursoComCodigoExistente() {
        var request = """
            {
              "codigo": "CURSO123",
              "titulo": "Curso Duplicado",
              "descricao": "Descrição",
              "cargaHoraria": 20
            }
        """;

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409);
    }
    @Test
    @Order(3)
    void deveBuscarCursoPorCodigoComSucesso() {
        webTestClient.get()
                .uri("/CURSO123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.codigo").isEqualTo("CURSO123")
                .jsonPath("$.titulo").isEqualTo("Spring Boot Completo");
    }

    @Test
    @Order(4)
    void deveBuscarCursoPorDescricaoComSucesso() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("descricao", "a")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].codigo").isEqualTo("CURSO123")
                .jsonPath("$[0].descricao").isEqualTo("Aprenda Spring Boot do zero");
    }

    // ------------------------------------------------------
    @Test
    @Order(5)
    void deveAtualizarCursoComSucesso() {
        var request = """
            {
              "codigoNovo": "CURSO456",
              "titulo": "Spring Boot Avançado",
              "descricao": "API Rest Avançada com Spring Boot",
              "cargaHoraria": 50
            }
        """;

        webTestClient.put()
                .uri("/CURSO123")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.codigo").isEqualTo("CURSO456")
                .jsonPath("$.cargaHoraria").isEqualTo(50);
    }

    // ------------------------------------------------------
    @Test
    @Order(6)
    void deveListarTodosOsCursosComSucesso() {
        webTestClient.get()
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].codigo").isEqualTo("CURSO456");
    }

    // ------------------------------------------------------
    @Test
    @Order(7)
    void deveDeletarCursoComSucesso() {
        webTestClient.delete()
                .uri("/CURSO456")
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    void deveLancarErroAoBuscarCursoInexistente() {
        webTestClient.get()
                .uri("/CURSO999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deveLancarErroAoAtualizarCursoInexistente() {
        var request = """
            {
              "codigoNovo": "CURSO999",
              "titulo": "Curso Inexistente",
              "descricao": "Descrição",
              "cargaHoraria": 10
            }
        """;

        webTestClient.put()
                .uri("/CURSO999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deveLancarErroAoDeletarCursoInexistente() {
        webTestClient.delete()
                .uri("/CURSO999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deveLancarErroAoCriarCursoComDadosInvalidos() {
        var request = """
            {
              "codigo": "",
              "titulo": "Curso inválido",
              "descricao": "Este curso é inválido pq o código está vazio",
              "cargaHoraria": 30
            }
        """;

        webTestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}