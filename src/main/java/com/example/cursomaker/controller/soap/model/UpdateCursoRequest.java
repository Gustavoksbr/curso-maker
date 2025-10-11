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
@XmlRootElement(name = "updateCursoRequest", namespace = "https://cursos-api-7vr6.onrender.com")
public class UpdateCursoRequest {
    @XmlElement(name = "codigo", namespace = "https://cursos-api-7vr6.onrender.com", required = true)
    private String codigo;
    @XmlElement(name = "codigoNovo", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String codigoNovo;
    @XmlElement(name = "titulo", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String titulo;
    @XmlElement(name = "descricao", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String descricao;
    @XmlElement(name = "cargaHoraria", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private Long cargaHoraria;

}
