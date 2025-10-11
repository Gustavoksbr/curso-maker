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
@XmlRootElement(name = "getCursoRequest", namespace = "https://cursos-api-7vr6.onrender.com")
public class GetCursoRequest {

    @XmlElement(name = "codigo", namespace = "https://cursos-api-7vr6.onrender.com", required = true)
    private String codigo;
}