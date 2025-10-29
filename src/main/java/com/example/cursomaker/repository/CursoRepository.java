package com.example.cursomaker.repository;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.exceptions.Erro409;
import com.example.cursomaker.exceptions.CursoNaoEncontrado;
import com.example.cursomaker.exceptions.NenhumCursoEncontrado;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CursoRepository {


    private final MongoTemplate mongoTemplate;
    private final CursoMongoRepository cursoMongoRepository;
    @Autowired
    public CursoRepository(CursoMongoRepository cursoMongoRepository, MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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
        try {
            cursoMongoRepository.save(cursoParaCriar);
        } catch (DuplicateKeyException e) {
            throw new Erro409("Curso com o mesmo código já existente");
        }
        return curso;
    }

    public void delete(String codigo) {
        long deletedCount = cursoMongoRepository.deleteByCodigo(codigo);
        if (deletedCount == 0) {
            throw new CursoNaoEncontrado("Curso não encontrado");
        }
    }

// obs: especificamente no metodo de update, optei por usar o MongoTemplate em vez do MongoRepository,
// pois com o MongoRepository eu precisava fazer duas consultas (uma para buscar o curso e outra para salvar as alterações),
// enquanto com o MongoTemplate eu consigo fazer tudo em uma única operação atômica, usando o findAndModify.
// isso melhora a performance e evita problemas de race conditions

    public Curso update(CursoParaAtualizar cursoParaAtualizar) {
        Query query = new Query(Criteria.where("codigo").is(cursoParaAtualizar.getCodigo()));
        Update update = getUpdate(cursoParaAtualizar);
        FindAndModifyOptions options = new FindAndModifyOptions()
                .returnNew(true)
                .upsert(false);
        CursoEntity cursoAtualizado;
        try {
            cursoAtualizado = mongoTemplate.findAndModify(query, update, options, CursoEntity.class);
        } catch (DuplicateKeyException e) {
            throw new Erro409("Curso com o mesmo código já existente");
        }
        if (cursoAtualizado == null) {
            throw new CursoNaoEncontrado("Curso não encontrado");
        }
        return cursoAtualizado.toDomain();
    }

    private Update getUpdate(CursoParaAtualizar cursoParaAtualizar) {
        Update update = new Update();
        if (cursoParaAtualizar.getCodigoNovo() != null && !cursoParaAtualizar.getCodigoNovo().isBlank())
            update.set("codigo", cursoParaAtualizar.getCodigoNovo());
        if (cursoParaAtualizar.getTitulo() != null && !cursoParaAtualizar.getTitulo().isBlank())
            update.set("titulo", cursoParaAtualizar.getTitulo());
        if (cursoParaAtualizar.getDescricao() != null && !cursoParaAtualizar.getDescricao().isBlank())
            update.set("descricao", cursoParaAtualizar.getDescricao());
        if (cursoParaAtualizar.getCargaHoraria() != null)
            update.set("carga_horaria", cursoParaAtualizar.getCargaHoraria());
        return update;
    }

}
