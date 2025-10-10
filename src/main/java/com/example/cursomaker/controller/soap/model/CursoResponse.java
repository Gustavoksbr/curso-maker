package com.example.cursomaker.controller.soap.model;

import com.example.cursomaker.dominio.Curso;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CursoResponse", namespace = "http://exemplo.com/cursos")
public class CursoResponse {

    @XmlElement(required = true)
    private String codigo;

    private String titulo;
    private String descricao;
    private Long cargaHoraria;

    CursoResponse( Curso curso){
        this.codigo = curso.getCodigo();
        this.titulo = curso.getTitulo();
        this.descricao = curso.getDescricao();
        this.cargaHoraria = curso.getCargaHoraria();
    }
}