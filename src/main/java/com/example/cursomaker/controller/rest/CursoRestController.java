package com.example.cursomaker.controller.rest;

import com.example.cursomaker.controller.rest.model.CursoParaAtualizar;
import com.example.cursomaker.controller.rest.model.CursoParaCriar;
import com.example.cursomaker.dominio.Curso;
import com.example.cursomaker.dominio.CursoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Curso> createCurso(@RequestBody @Valid CursoParaCriar curso) {
        Curso createdCurso = cursoService.create(curso.toDomain());
        return ResponseEntity.status(201).body(createdCurso);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Curso> updateCurso(@RequestBody @Valid CursoParaAtualizar cursoRequest, @PathVariable String codigo) {
        if (cursoRequest.getCodigoNovo() == null) {
            cursoRequest.setCodigoNovo(codigo);
        }
        Curso curso = new Curso(cursoRequest.getCodigoNovo(), cursoRequest.getTitulo(), cursoRequest.getDescricao(), cursoRequest.getCargaHoraria());
        Curso updatedCurso = cursoService.update(curso, codigo);
        return ResponseEntity.ok(updatedCurso);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deleteCurso(@PathVariable String codigo) {
        cursoService.delete(codigo);
        return ResponseEntity.noContent().build();
    }
}
