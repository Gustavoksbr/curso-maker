package com.example.cursomaker.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Curso {
    private String codigo;
    private String titulo;
    private String descricao;
    private Long cargaHoraria;
}
