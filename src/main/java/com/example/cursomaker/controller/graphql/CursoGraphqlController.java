package com.example.cursomaker.controller.graphql;

import com.example.cursomaker.controller.validator.CursoValidator;
import com.example.cursomaker.dominio.Curso;
import com.example.cursomaker.dominio.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Profile("api-graphql")
@Controller
public class CursoGraphqlController {
    private final CursoService cursoService;
    private final CursoValidator cursoValidator;
    @Autowired
    public CursoGraphqlController(CursoService cursoService, CursoValidator cursoValidator) {
        this.cursoService = cursoService;
        this.cursoValidator = cursoValidator;
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
      cursoValidator.validarCriacao(curso);
      return cursoService.create(curso);
  }
    @MutationMapping
    public Curso updateCurso(@Argument String codigo, @Argument String codigoNovo,  @Argument String titulo, @Argument String descricao, @Argument Long cargaHoraria) {
        if(codigoNovo == null){
            codigoNovo = codigo;
        }
        Curso curso = new Curso(codigoNovo, titulo, descricao, cargaHoraria);
        cursoValidator.validarAtualizacao(curso, codigoNovo);
        return cursoService.update(curso, codigo);
    }
    @MutationMapping
    public Boolean deleteCurso(@Argument String codigo) {
        cursoService.delete(codigo);
        return true;
    }
}
