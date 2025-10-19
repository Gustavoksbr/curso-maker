package com.example.cursomaker.repository;

import com.example.cursomaker.domain.model.Curso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursoEntity {
    @Id
    private String id;
    @Field("codigo")
    @Indexed(unique = true)
    private String codigo;
    @Field("titulo")
    private String titulo;
    @Field("descricao")
    private String descricao;
    @Field("carga_horaria")
    private Long cargaHoraria;

    CursoEntity (Curso curso) {
        this.codigo = curso.getCodigo();
        this.titulo = curso.getTitulo();
        this.descricao = curso.getDescricao();
        this.cargaHoraria = curso.getCargaHoraria();
    }

    public Curso toDomain(){
        return new Curso(codigo, titulo, descricao, cargaHoraria);
    }
}
