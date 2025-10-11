package com.example.cursomaker.controller.soap.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "createCursoRequest", namespace = "https://cursos-api-7vr6.onrender.com")
public class CreateCursoRequest {
    @XmlElement(name = "codigo", namespace = "https://cursos-api-7vr6.onrender.com", required = true)
    @NotBlank
    @Size(max = 15, message = "O código deve ter no máximo 15 caracteres")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ0-9 ]+$", message = "O código deve conter apenas letras e números")
    private String codigo;
    @XmlElement(name = "titulo", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String titulo;
    @XmlElement(name = "descricao", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private String descricao;
    @XmlElement(name = "cargaHoraria", namespace = "https://cursos-api-7vr6.onrender.com", required = false)
    private Long cargaHoraria;
}
