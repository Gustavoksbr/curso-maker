package com.example.cursomaker.controller.rest.model;

import com.example.cursomaker.dominio.Curso;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoParaAtualizar {
    @Size(max = 15, message = "O código deve ter no máximo 15 caracteres")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ0-9 ]+$", message = "O código deve conter apenas letras e números")
    private String codigoNovo;
    @Size(max = 30, message = "O título deve ter no máximo 100 caracteres")
    private String titulo;
    @Size(max = 100, message = "A descrição deve ter no máximo 255 caracteres")
    private String descricao;
    @Min(1)
    @Max(300)
    private Long cargaHoraria;

    public Curso toDomain(){
        return new Curso(codigoNovo, titulo, descricao, cargaHoraria);
    }
}
