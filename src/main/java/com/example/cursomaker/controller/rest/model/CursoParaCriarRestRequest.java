package com.example.cursomaker.controller.rest.model;

import com.example.cursomaker.domain.model.Curso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoParaCriarRestRequest {
    private String codigo;
    private String titulo;
    private String descricao;
    private Long cargaHoraria;

    public Curso toDomain(){
        return new Curso(codigo, titulo, descricao, cargaHoraria);
    }
}
