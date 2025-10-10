package com.example.cursomaker.controller.soap.model;

import com.example.cursomaker.dominio.Curso;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "updateCursoRequest", namespace = "http://exemplo.com/cursos")
public class UpdateCursoRequest {
    @XmlElement(name = "codigo", namespace = "http://exemplo.com/cursos", required = true)
    private String codigo;
    @XmlElement(name = "codigoNovo", namespace = "http://exemplo.com/cursos", required = false)
    private String codigoNovo;
    @XmlElement(name = "titulo", namespace = "http://exemplo.com/cursos", required = false)
    private String titulo;
    @XmlElement(name = "descricao", namespace = "http://exemplo.com/cursos", required = false)
    private String descricao;
    @XmlElement(name = "cargaHoraria", namespace = "http://exemplo.com/cursos", required = false)
    private Long cargaHoraria;

}
