package com.example.cursomaker.controller.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "listCursosResponse", namespace = "http://exemplo.com/cursos")
public class ListCursosResponse {
    @XmlElement(name = "cursos")
    private List<CursoResponse> cursos = new ArrayList<>();
}
