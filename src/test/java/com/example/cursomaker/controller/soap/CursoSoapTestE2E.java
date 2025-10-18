package com.example.cursomaker.controller.soap;

import com.example.cursomaker.repository.CursoMongoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CursoSoapTestE2E {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private CursoMongoRepository cursoMongoRepository;

    @BeforeEach
    void setupClient() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/ws/cursos")
                .build();
    }
    @BeforeAll
    @AfterAll
    void limparTudo(){
        cursoMongoRepository.deleteAll();
    }
    @Test
    @Order(1)
    void deveCriarCursoComSucessoComSucesso() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:createCursoRequest>
                            <cur:codigo>CURSO123</cur:codigo>
                            <cur:titulo>Spring Boot Completo</cur:titulo>
                            <cur:descricao>Aprenda Spring Boot do zero</cur:descricao>
                            <cur:cargaHoraria>40</cur:cargaHoraria>
                        </cur:createCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("<codigo>CURSO123</codigo>")));
    }
    @Test
    @Order(2)
    void deveLancarErroAoCriarCursoComCodigoExistente() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:createCursoRequest>
                            <cur:codigo>CURSO123</cur:codigo>
                            <cur:titulo>Curso Duplicado</cur:titulo>
                            <cur:descricao>Descrição</cur:descricao>
                            <cur:cargaHoraria>20</cur:cargaHoraria>
                        </cur:createCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Curso com o mesmo código já existente")));
    }
    @Test
    @Order(3)
    void deveBuscarCursoPorCodigoComSucesso() {
        var request = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Header/>
                    <soap:Body>
                        <cur:getCursoRequest>
                            <cur:codigo>CURSO123</cur:codigo>
                        </cur:getCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertTrue(response.contains("<codigo>CURSO123</codigo>"));
                    assertTrue(response.contains("<titulo>Spring Boot Completo</titulo>"));
                });
    }

    @Test
    @Order(4)
    void deveBuscarCursoPorDescricaoComSucesso() {
        var request = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Header/>
                    <soap:Body>
                        <cur:procurarCursosRequest>
                            <cur:descricao>Spring Boot</cur:descricao>
                        </cur:procurarCursosRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertTrue(response.contains("<codigo>CURSO123</codigo>"));
                    assertTrue(response.contains("<descricao>Aprenda Spring Boot do zero</descricao>"));
                });
    }

    @Test
    @Order(5)
    void deveAtualizarCursoComSucesso() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:updateCursoRequest>
                            <cur:codigo>CURSO123</cur:codigo>
                            <cur:codigoNovo>CURSO456</cur:codigoNovo>
                            <cur:titulo>Spring Boot Avançado</cur:titulo>
                            <cur:descricao>Aprenda Spring Boot em profundidade</cur:descricao>
                            <cur:cargaHoraria>60</cur:cargaHoraria>
                        </cur:updateCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertTrue(response.contains("<codigo>CURSO456</codigo>"));
                    assertTrue(response.contains("<titulo>Spring Boot Avançado</titulo>"));
                });
    }

    @Test
    @Order(6)
    void deveBuscarTodosOsCursosComSucesso() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:listCursosRequest/>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("<cursos>")));
    }

    @Test
    @Order(7)
    void deveExcluirCursoComSucesso() {
        var deleteRequest = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:deleteCursoRequest>
                            <cur:codigo>CURSO456</cur:codigo>
                        </cur:deleteCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(deleteRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("<ns2:success>true</ns2:success>")));
    }

    @Test
    void deveLancarErroAoBuscarCursoInexistente() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:getCursoRequest>
                            <cur:codigo>CURSO999</cur:codigo>
                        </cur:getCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Curso não encontrado")));
    }

    @Test
    void deveLancarErroAoAtualizarCursoInexistente() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:updateCursoRequest>
                            <cur:codigo>CURSO999</cur:codigo>
                            <cur:codigoNovo>CURSO999</cur:codigoNovo>
                            <cur:titulo>Curso Inexistente</cur:titulo>
                            <cur:descricao>Descrição</cur:descricao>
                            <cur:cargaHoraria>10</cur:cargaHoraria>
                        </cur:updateCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Curso não encontrado")));
    }

    @Test
    void deveLancarErroAoDeletarCursoInexistente() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:deleteCursoRequest>
                            <cur:codigo>CURSO999</cur:codigo>
                        </cur:deleteCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Curso não encontrado")));
    }



    @Test
    void deveLancarErroAoCriarCursoComDadosInvalidos() {
        var request = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:cur="https://cursos-api-7vr6.onrender.com">
                    <soap:Body>
                        <cur:createCursoRequest>
                            <cur:codigo></cur:codigo>
                            <cur:titulo>Curso inválido</cur:titulo>
                            <cur:descricao>Este curso é inválido pq o código está vazio</cur:descricao>
                            <cur:cargaHoraria>30</cur:cargaHoraria>
                        </cur:createCursoRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        webTestClient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Campo 'codigo': não deve estar em branco")));
    }
}