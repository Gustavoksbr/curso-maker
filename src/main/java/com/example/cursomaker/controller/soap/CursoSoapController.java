package com.example.cursomaker.controller.soap;
import com.example.cursomaker.controller.soap.model.*;
import com.example.cursomaker.controller.validator.CursoValidator;
import com.example.cursomaker.dominio.Curso;
import com.example.cursomaker.dominio.CursoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
@Profile("api-soap")
public class CursoSoapController{

    private static final String NAMESPACE_URI = "https://cursos-api-7vr6.onrender.com";

    private final CursoService cursoService;
    private final CursoValidator cursoValidator;

    @Autowired
    public CursoSoapController(CursoService cursoService, CursoValidator cursoValidator) {
        this.cursoService = cursoService;
        this.cursoValidator = cursoValidator;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCursoRequest")
    @ResponsePayload
    public CursoResponse getCurso(@Valid @RequestPayload GetCursoRequest request) {
        Curso curso = cursoService.findByCodigo(request.getCodigo());
        return toResponse(curso);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listCursosRequest")
    @ResponsePayload
    public ListCursosResponse listCursos(
    ) {
        List<Curso> cursos = cursoService.findAll();
        ListCursosResponse response = new ListCursosResponse();
        for (Curso c : cursos) {
            response.getCursos().add(toResponse(c));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI,localPart = "procurarCursosRequest")
    @ResponsePayload
    public ListCursosResponse  procurarCursos(@Valid @RequestPayload ProcurarCursosRequest request) {
        List<Curso> cursos = cursoService.findByParametros(
                request.getTitulo(),
                request.getDescricao(),
                request.getMinCargaHoraria(),
                request.getMaxCargaHoraria()
        );
        ListCursosResponse response = new ListCursosResponse();
        for (Curso c : cursos) {
            response.getCursos().add(toResponse(c));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createCursoRequest")
    @ResponsePayload
    public CursoResponse createCurso(@Valid @RequestPayload CreateCursoRequest request) {
        Curso cursoAValidar = new Curso(request.getCodigo(), request.getTitulo(), request.getDescricao(), request.getCargaHoraria());
        cursoValidator.validarCriacao(cursoAValidar);
        Curso cursoARetornar = cursoService.create(
                new Curso(request.getCodigo(), request.getTitulo(), request.getDescricao(), request.getCargaHoraria())
        );
        CursoResponse response = new CursoResponse();
        copyCurso(cursoARetornar, response);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCursoRequest")
    @ResponsePayload
    public CursoResponse updateCurso(@Valid @RequestPayload UpdateCursoRequest request) {
        if (request.getCodigoNovo() == null || request.getCodigoNovo().isEmpty()) {
            request.setCodigoNovo(request.getCodigo());
        }
        Curso curso = new Curso(request.getCodigoNovo(), request.getTitulo(), request.getDescricao(), request.getCargaHoraria());
        cursoValidator.validarAtualizacao(curso, request.getCodigoNovo());
        Curso atualizado = cursoService.update(curso, request.getCodigo());
        CursoResponse response = new CursoResponse();
        copyCurso(atualizado, response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteCursoRequest")
    @ResponsePayload
    public DeleteCursoResponse deleteCurso(@Valid @RequestPayload DeleteCursoRequest request) {
        cursoService.delete(request.getCodigo());
        DeleteCursoResponse response = new DeleteCursoResponse();
        response.setSuccess(true);
        return response;
    }

    private CursoResponse toResponse(Curso curso) {
        CursoResponse resp = new CursoResponse();
        copyCurso(curso, resp);
        return resp;
    }

    private void copyCurso(Curso curso, CursoResponse resp) {
        resp.setCodigo(curso.getCodigo());
        resp.setTitulo(curso.getTitulo());
        resp.setDescricao(curso.getDescricao());
        resp.setCargaHoraria(curso.getCargaHoraria());
    }
}