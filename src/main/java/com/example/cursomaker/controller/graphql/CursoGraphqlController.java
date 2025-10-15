package com.example.cursomaker.controller.graphql;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.CursoService;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class CursoGraphqlController {
    private final CursoService cursoService;
    @Autowired
    public CursoGraphqlController(CursoService cursoService) {
        this.cursoService = cursoService;
    }
    @QueryMapping
    public List<Curso> listCursos(@Argument String titulo,
                                  @Argument String descricao,
                                  @Argument Long minCargaHoraria,
                                  @Argument Long maxCargaHoraria) {
        if (titulo != null || minCargaHoraria != null || descricao != null || maxCargaHoraria != null) {
            return cursoService.findByParametros(titulo, descricao, minCargaHoraria, maxCargaHoraria);
        }
        return cursoService.findAll();
    }

    @QueryMapping
    public Curso getCursoByCodigo(@Argument String codigo) {
        return cursoService.findByCodigo(codigo);
    }
  @MutationMapping
  public Curso createCurso(@Argument String codigo, @Argument String titulo, @Argument String descricao, @Argument Long cargaHoraria) {
      Curso curso = new Curso(codigo, titulo, descricao, cargaHoraria);
      return cursoService.create(curso);
  }
    @MutationMapping
    public Curso updateCurso(@Argument String codigo, @Argument String codigoNovo,  @Argument String titulo, @Argument String descricao, @Argument Long cargaHoraria) {
        CursoParaAtualizar curso = CursoParaAtualizar.builder().codigo(codigo).codigoNovo(codigoNovo).titulo(titulo).descricao(descricao).cargaHoraria(cargaHoraria).build();
        return cursoService.update(curso);
    }
    @MutationMapping
    public Boolean deleteCurso(@Argument String codigo) {
        cursoService.delete(codigo);
        return true;
    }
}
