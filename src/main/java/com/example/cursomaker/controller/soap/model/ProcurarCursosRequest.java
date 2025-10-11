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
@XmlRootElement(name = "procurarCursosRequest", namespace = "https://cursos-api-7vr6.onrender.com")
public class ProcurarCursosRequest {
    @XmlElement(name = "titulo", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String titulo;
    @XmlElement(name = "descricao", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String descricao;
    @XmlElement(name = "minCargaHoraria", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private Long minCargaHoraria;
    @XmlElement(name = "maxCargaHoraria", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private Long maxCargaHoraria;
}
