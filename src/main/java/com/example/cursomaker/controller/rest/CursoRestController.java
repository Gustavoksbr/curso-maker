package com.example.cursomaker.controller.rest;

import com.example.cursomaker.controller.rest.model.CursoParaAtualizarRestRequest;
import com.example.cursomaker.controller.rest.model.CursoParaCriarRestRequest;
import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.CursoService;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@Profile("api-rest")
public class CursoRestController {

    private CursoService cursoService;
    @Autowired
    public CursoRestController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

   @GetMapping()
    public ResponseEntity<List<Curso>> cursos(@RequestParam (required = false) String titulo,
                                              @RequestParam (required = false) String descricao,
                                              @RequestParam (required = false) Long minCargaHoraria,
                                              @RequestParam (required = false) Long maxCargaHoraria) {
         if (titulo != null || minCargaHoraria != null || descricao != null || maxCargaHoraria != null) {
                return ResponseEntity.ok(cursoService.findByParametros(titulo, descricao, minCargaHoraria, maxCargaHoraria));
         }
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Curso> getCurso(@PathVariable String codigo) {
        return ResponseEntity.ok(cursoService.findByCodigo(codigo));
    }

    @PostMapping()
    public ResponseEntity<Curso> createCurso(@RequestBody CursoParaCriarRestRequest curso) {
        Curso createdCurso = cursoService.create(curso.toDomain());
        return ResponseEntity.status(201).body(createdCurso);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Curso> updateCurso(@RequestBody CursoParaAtualizarRestRequest cursoRequest, @PathVariable String codigo) {
        CursoParaAtualizar cursoParaAtualizar = CursoParaAtualizar.builder()
                .codigo(codigo)
                .codigoNovo(cursoRequest.getCodigoNovo())
                .titulo(cursoRequest.getTitulo())
                .descricao(cursoRequest.getDescricao())
                .cargaHoraria(cursoRequest.getCargaHoraria())
                .build();
        Curso updatedCurso = cursoService.update(cursoParaAtualizar);
        return ResponseEntity.ok(updatedCurso);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deleteCurso(@PathVariable String codigo) {
        cursoService.delete(codigo);
        return ResponseEntity.noContent().build();
    }
}
