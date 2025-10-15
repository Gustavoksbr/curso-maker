package com.example.cursomaker.controller.soap;
import com.example.cursomaker.controller.soap.model.*;
import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.CursoService;

import com.example.cursomaker.domain.model.CursoParaAtualizar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint

public class CursoSoapController{

    private static final String NAMESPACE_URI = "https://cursos-api-7vr6.onrender.com";

    private final CursoService cursoService;

    @Autowired
    public CursoSoapController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCursoRequest")
    @ResponsePayload
    public CursoResponse getCurso(@RequestPayload GetCursoRequest request) {
        Curso curso = cursoService.findByCodigo(request.getCodigo());
        return new CursoResponse(curso);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listCursosRequest")
    @ResponsePayload
    public ListCursosResponse listCursos(
    ) {
        List<Curso> cursos = cursoService.findAll();
        ListCursosResponse response = new ListCursosResponse();
        for (Curso curso : cursos) {
            response.getCursos().add(new CursoResponse(curso));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI,localPart = "procurarCursosRequest")
    @ResponsePayload
    public ListCursosResponse  procurarCursos(@RequestPayload ProcurarCursosRequest request) {
        List<Curso> cursos = cursoService.findByParametros(
                request.getTitulo(),
                request.getDescricao(),
                request.getMinCargaHoraria(),
                request.getMaxCargaHoraria()
        );
        ListCursosResponse response = new ListCursosResponse();
        for (Curso curso : cursos) {
            response.getCursos().add(new CursoResponse(curso));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createCursoRequest")
    @ResponsePayload
    public CursoResponse createCurso(@RequestPayload CreateCursoRequest request) {
        Curso cursoParaCriar = new Curso(request.getCodigo(), request.getTitulo(), request.getDescricao(), request.getCargaHoraria());
        Curso cursoARetornar = cursoService.create(cursoParaCriar);
        return new CursoResponse(cursoARetornar);
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCursoRequest")
    @ResponsePayload
    public CursoResponse updateCurso(@RequestPayload UpdateCursoRequest request) {
        CursoParaAtualizar cursoParaAtualizar = CursoParaAtualizar.builder().codigo(request.getCodigo()).codigoNovo(request.getCodigoNovo()).titulo(request.getTitulo()).descricao(request.getDescricao()).build();
        Curso atualizado = cursoService.update(cursoParaAtualizar);
        return new CursoResponse(atualizado);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteCursoRequest")
    @ResponsePayload
    public DeleteCursoResponse deleteCurso(@RequestPayload DeleteCursoRequest request) {
        cursoService.delete(request.getCodigo());
        DeleteCursoResponse response = new DeleteCursoResponse();
        response.setSuccess(true);
        return response;
    }


}