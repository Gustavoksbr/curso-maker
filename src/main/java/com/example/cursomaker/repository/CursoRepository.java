package com.example.cursomaker.repository;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.exceptions.Erro409;
import com.example.cursomaker.exceptions.CursoNaoEncontrado;
import com.example.cursomaker.exceptions.NenhumCursoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CursoRepository {

    private final CursoMongoRepository cursoMongoRepository;
    @Autowired
    public CursoRepository(CursoMongoRepository cursoMongoRepository) {
        this.cursoMongoRepository = cursoMongoRepository;
    }
    public List<Curso> findAll() {
        return this.cursoMongoRepository.findAll().stream().map(CursoEntity::toDomain).toList();
    }
   public List<Curso> findByParametros(String titulo, String descricao, Long minCargaHoraria, Long maxCargaHoraria) {
        List<Curso> cursos = this.cursoMongoRepository.findAllByTituloContainingIgnoreCaseAndDescricaoContainingIgnoreCaseAndCargaHorariaBetween(
                titulo,
                descricao,
                minCargaHoraria,
                maxCargaHoraria
        ).stream().map(CursoEntity::toDomain).toList();

        if (cursos.isEmpty()) {
            throw new NenhumCursoEncontrado("Nenhum curso encontrado com os parâmetros fornecidos");
        }

        return cursos;
    }
    public Curso findByCodigo(String codigo) {
        return this.cursoMongoRepository.findByCodigo(codigo).map(CursoEntity::toDomain).orElseThrow(()-> new CursoNaoEncontrado("Curso não encontrado"));
    }
    public Curso create(Curso curso) {
        CursoEntity cursoParaCriar = new CursoEntity(curso);
        if (cursoMongoRepository.existsByCodigo(curso.getCodigo())) {
            throw new Erro409("Curso com o mesmo código ja existente");
        }
        this.cursoMongoRepository.save(cursoParaCriar);
        return curso;
    }
    public Curso update(CursoParaAtualizar dto) {
        CursoEntity entity = this.cursoMongoRepository.findByCodigo(dto.getCodigo())
                .orElseThrow(() -> new CursoNaoEncontrado("Curso não encontrado"));

        String codigoFinal = dto.getCodigo();

        if (dto.getCodigoNovo() != null &&
                !dto.getCodigoNovo().isBlank() &&
                !dto.getCodigoNovo().equals(dto.getCodigo())) {

            if (this.cursoMongoRepository.existsByCodigo(dto.getCodigoNovo())) {
                throw new Erro409("Já existe um curso com o código novo fornecido");
            }

            codigoFinal = dto.getCodigoNovo();
        }
        entity.update(Curso.builder()
                .codigo(codigoFinal)
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .cargaHoraria(dto.getCargaHoraria())
                .build());

        this.cursoMongoRepository.save(entity);
        return entity.toDomain();
    }
    public void delete(String codigo) {
        if(!this.cursoMongoRepository.existsByCodigo(codigo)){
            throw new CursoNaoEncontrado("Curso não encontrado");
        }
        this.cursoMongoRepository.deleteByCodigo(codigo);
    }
}
