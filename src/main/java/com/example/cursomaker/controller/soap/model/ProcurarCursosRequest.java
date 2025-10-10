package com.example.cursomaker.controller.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "procurarCursosRequest", namespace = "http://exemplo.com/cursos")
public class ProcurarCursosRequest {
    @XmlElement(name = "titulo", namespace = "http://exemplo.com/cursos", required = false)
    private String titulo;
    @XmlElement(name = "descricao", namespace = "http://exemplo.com/cursos", required = false)
    private String descricao;
    @XmlElement(name = "minCargaHoraria", namespace = "http://exemplo.com/cursos", required = false)
    private Long minCargaHoraria;
    @XmlElement(name = "maxCargaHoraria", namespace = "http://exemplo.com/cursos", required = false)
    private Long maxCargaHoraria;
}
