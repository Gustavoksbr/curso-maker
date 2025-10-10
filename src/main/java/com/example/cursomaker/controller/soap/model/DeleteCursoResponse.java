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
@XmlRootElement(name = "deleteCursoResponse", namespace = "http://exemplo.com/cursos")
public class DeleteCursoResponse {
    @XmlElement(name = "success", namespace = "http://exemplo.com/cursos", required = true)
    private Boolean success;
}
