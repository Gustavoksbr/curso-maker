package com.example.cursomaker.domain;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoValidator cursoValidator;
    @Autowired
    public CursoService(CursoRepository cursoRepository, CursoValidator cursoValidator) {
        this.cursoRepository = cursoRepository;
        this.cursoValidator = cursoValidator;
    }
    public List<Curso> findAll() {
       return this.cursoRepository.findAll();
    }
    public List<Curso> findByParametros(String titulo, String descricao,
                                              Long minCargaHoraria, Long maxCargaHoraria) {
        if (titulo == null) titulo = "";
        if (descricao == null) descricao = "";
        if (minCargaHoraria == null) minCargaHoraria = 0L;
        if (maxCargaHoraria == null) maxCargaHoraria = Long.MAX_VALUE;

        return this.cursoRepository.findByParametros(
                titulo, descricao, minCargaHoraria, maxCargaHoraria
        );
    }
    public Curso findByCodigo(String codigo) {
        return this.cursoRepository.findByCodigo(codigo);
    }
    public Curso create(Curso curso) {
        cursoValidator.validarCriacao(curso);
        return this.cursoRepository.create(curso);
    }
    public Curso update(CursoParaAtualizar curso) {
        cursoValidator.validarAtualizacao(curso);
        return this.cursoRepository.update(curso);
    }
    public void delete(String codigo) {
        this.cursoRepository.delete(codigo);
    }
}
