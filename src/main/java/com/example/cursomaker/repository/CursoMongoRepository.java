package com.example.cursomaker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface CursoMongoRepository  extends MongoRepository<CursoEntity, String> {
    Optional<CursoEntity> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<CursoEntity> findAllByTituloContainingIgnoreCaseAndDescricaoContainingIgnoreCaseAndCargaHorariaBetween(
            String titulo, String descricao, Long minCargaHoraria, Long maxCargaHoraria
    );

}
