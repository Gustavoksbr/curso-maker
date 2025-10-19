package com.example.cursomaker.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoMongoRepository  extends MongoRepository<CursoEntity, String> {


    Optional<CursoEntity> findByCodigo(String codigo);
    long deleteByCodigo(String codigo);
    List<CursoEntity> findAllByTituloContainingIgnoreCaseAndDescricaoContainingIgnoreCaseAndCargaHorariaBetween(
            String titulo, String descricao, Long minCargaHoraria, Long maxCargaHoraria
    );
}
