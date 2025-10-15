package com.example.cursomaker.repository;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.exceptions.CursoNaoEncontrado;
import com.example.cursomaker.exceptions.Erro409;
import com.example.cursomaker.exceptions.NenhumCursoEncontrado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CursoRepositoryTestIntegration {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoMongoRepository cursoMongoRepository;

    @BeforeEach
    void cleanDatabase() {
        cursoMongoRepository.deleteAll(); // Garante base limpa antes de cada teste
    }

    // ✅ Representa que espera-se sucesso
    // ⚠️ Representa que espera-se uma falha

    // ----------------------------------------------------
    // ✅ Cenário 1: criação e busca por código
    // ----------------------------------------------------
    @Test
    void deveCriarEEncontrarCurso() {
        var curso = Curso.builder()
                .codigo("CURSO123")
                .titulo("Curso de Teste")
                .descricao("Descrição do curso de teste")
                .cargaHoraria(40L)
                .build();

        Curso cursoCriado = cursoRepository.create(curso);

        Curso cursoEncontrado = cursoRepository.findByCodigo("CURSO123");

        assertEquals(cursoCriado.getDescricao(), cursoEncontrado.getDescricao());
        assertEquals("Curso de Teste", cursoEncontrado.getTitulo());
    }

    // ----------------------------------------------------
    // ⚠️ Cenário 2: criação com código duplicado
    // ----------------------------------------------------
    @Test
    void deveLancarErroAoCriarCursoComCodigoDuplicado() {
        var curso = Curso.builder()
                .codigo("CURSO123")
                .titulo("Curso 1")
                .descricao("Descrição 1")
                .cargaHoraria(20L)
                .build();

        cursoRepository.create(curso);

        var cursoDuplicado = Curso.builder()
                .codigo("CURSO123")
                .titulo("Curso 2")
                .descricao("Descrição 2")
                .cargaHoraria(30L)
                .build();

        assertThrows(Erro409.class, () -> cursoRepository.create(cursoDuplicado));
    }

    // ----------------------------------------------------
    // ✅ Cenário 3: busca por parâmetros
    // ----------------------------------------------------
    @Test
    void deveBuscarPorParametros() {
        cursoRepository.create(new Curso("C1", "Java", "Backend", 40L));
        cursoRepository.create(new Curso("C2", "Spring", "Framework Java", 60L));
        cursoRepository.create(new Curso("C3", "JavaScript", "Frontend", 50L));

        List<Curso> encontrados = cursoRepository.findByParametros("Java", "", 30L, 60L);

        assertEquals(2, encontrados.size());
        assertTrue(encontrados.stream().anyMatch(c -> c.getCodigo().equals("C1")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getCodigo().equals("C3")));
    }

    // ----------------------------------------------------
    // ⚠️ Cenário 4: busca por parâmetros sem resultados
    // ----------------------------------------------------
    @Test
    void deveLancarExcecaoQuandoNenhumCursoEncontrado() {
        cursoRepository.create(new Curso("C1", "Java", "Backend", 40L));

        assertThrows(NenhumCursoEncontrado.class,
                () -> cursoRepository.findByParametros("Python", "Data", 10L, 20L));
    }

    // ----------------------------------------------------
    // ✅ Cenário 5: atualizar curso com sucesso
    // ----------------------------------------------------
    @Test
    void deveAtualizarCursoComSucesso() {
        cursoRepository.create(new Curso("CURSO1", "Java", "Curso Antigo", 30L));

        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO1");
        dto.setCodigoNovo("CURSO1ATUALIZADO");
        dto.setTitulo("Java Atualizado");
        dto.setDescricao("Descrição Atualizada");
        dto.setCargaHoraria(50L);

        Curso atualizado = cursoRepository.update(dto);

        assertEquals("Java Atualizado", atualizado.getTitulo());
        assertEquals(50L, atualizado.getCargaHoraria());
    }
    // ----------------------------------------------------
    //  Cenário 6: procurar curso com codigo antigo
    // ----------------------------------------------------
    @Test
    void deveChecarAtualizacao() {
        cursoRepository.create(new Curso("CURSO1", "Java", "Curso Antigo", 30L));

        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO1");
        dto.setCodigoNovo("CURSO1ATUALIZADO");
        dto.setTitulo("Java Atualizado");
        dto.setDescricao("Descrição Atualizada");
        dto.setCargaHoraria(50L);

        cursoRepository.update(dto);

        assertThrows(CursoNaoEncontrado.class, () -> cursoRepository.findByCodigo("CURSO1"));
        Curso cursoEncontrado = cursoRepository.findByCodigo("CURSO1ATUALIZADO");
        assertNotNull(cursoEncontrado);
        assertEquals("CURSO1ATUALIZADO", cursoEncontrado.getCodigo());
        assertEquals("Java Atualizado", cursoEncontrado.getTitulo());
    }

    // ----------------------------------------------------
    // ⚠️ Cenário 7: tentar atualizar código para outro já existente
    // ----------------------------------------------------
    @Test
    void deveLancarErroAoAtualizarComCodigoDuplicado() {
        cursoRepository.create(new Curso("CURSO1", "Curso 1", "Descrição 1", 20L));
        cursoRepository.create(new Curso("CURSO2", "Curso 2", "Descrição 2", 30L));

        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO1");
        dto.setCodigoNovo("CURSO2");

        assertThrows(Erro409.class, () -> cursoRepository.update(dto));
    }

    // ----------------------------------------------------
    // ✅ Cenário 8: deletar curso existente
    // ----------------------------------------------------
    @Test
    void deveDeletarCursoComSucesso() {
        cursoRepository.create(new Curso("CURSO1", "Curso 1", "Descrição 1", 20L));
        cursoRepository.delete("CURSO1");

        assertThrows(CursoNaoEncontrado.class, () -> cursoRepository.findByCodigo("CURSO1"));
    }

    // ----------------------------------------------------
    // ⚠️ Cenário 9: deletar curso inexistente
    // ----------------------------------------------------
    @Test
    void deveLancarErroAoDeletarCursoInexistente() {
        assertThrows(CursoNaoEncontrado.class, () -> cursoRepository.delete("NAO_EXISTE"));
    }
}