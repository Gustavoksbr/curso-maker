package com.example.cursomaker.domain;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.exceptions.ErroDeRequisicaoGeral;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class CursoValidatorTestUnitary {
    @Autowired
    private CursoValidator validator;

    @Test
    void deveValidarCriacaoComSucesso() {
        Curso curso = new Curso("CURSO1", "Spring Boot", "Aprenda do zero", 40L);
        assertDoesNotThrow(() -> validator.validarCriacao(curso));
    }

    @Test
    void deveLancarErroQuandoCodigoForEmBrancoNaCriacao() {
        Curso curso = new Curso("   ", "Titulo", "Descricao", 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("codigo"));
    }

    @Test
    void deveLancarErroQuandoCodigoMaiorQue15NaCriacao() {
        Curso curso = new Curso("ABCDEFGHIJKLMNOP", "Titulo", "Descricao", 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("15 caracteres"));
    }

    @Test
    void deveLancarErroQuandoCodigoTemCaracteresInvalidosNaCriacao() {
        Curso curso = new Curso("CUR$O!", "Titulo", "Descricao", 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("apenas letras e números"));
    }

    @Test
    void deveLancarErroQuandoTituloEmBrancoNaCriacao() {
        Curso curso = new Curso("COD123", " ", "Descricao", 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("titulo"));
    }

    @Test
    void deveLancarErroQuandoTituloMaiorQue30NaCriacao() {
        String titulo = "A".repeat(31);
        Curso curso = new Curso("COD123", titulo, "Descricao", 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("30 caracteres"));
    }

    @Test
    void deveLancarErroQuandoDescricaoEmBrancoNaCriacao() {
        Curso curso = new Curso("COD123", "Titulo", " ", 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("descricao"));
    }

    @Test
    void deveLancarErroQuandoDescricaoMaiorQue100NaCriacao() {
        String desc = "A".repeat(101);
        Curso curso = new Curso("COD123", "Titulo", desc, 10L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("100 caracteres"));
    }

    @Test
    void deveLancarErroQuandoCargaHorariaNulaNaCriacao() {
        Curso curso = new Curso("COD123", "Titulo", "Descricao", null);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("cargaHoraria"));
    }

    @Test
    void deveLancarErroQuandoCargaHorariaMenorQue1NaCriacao() {
        Curso curso = new Curso("COD123", "Titulo", "Descricao", 0L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("1 e 300"));
    }

    @Test
    void deveLancarErroQuandoCargaHorariaMaiorQue300NaCriacao() {
        Curso curso = new Curso("COD123", "Titulo", "Descricao", 301L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarCriacao(curso));
        assertTrue(e.getMessage().contains("1 e 300"));
    }

    // --------------------------
    //  TESTES validarAtualizacao
    // --------------------------

    @Test
    void deveValidarAtualizacaoComSucesso() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setCodigoNovo("CURSO456");
        dto.setTitulo("Novo Título");
        dto.setDescricao("Nova Descrição");
        dto.setCargaHoraria(100L);
        assertDoesNotThrow(() -> validator.validarAtualizacao(dto));
    }

    @Test
    void deveLancarErroQuandoCodigoNaoInformadoNaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("  ");
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("atualizar"));
    }

    @Test
    void deveLancarErroQuandoCodigoNovoMaiorQue15NaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setCodigoNovo("ABCDEFGHIJKLMNOP");
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("15 caracteres"));
    }

    @Test
    void deveLancarErroQuandoCodigoNovoTemCaracteresInvalidosNaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setCodigoNovo("CUR$O");
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("apenas letras e números"));
    }

    @Test
    void deveLancarErroQuandoTituloMaiorQue30NaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setTitulo("A".repeat(31));
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("30 caracteres"));
    }

    @Test
    void deveLancarErroQuandoDescricaoMaiorQue100NaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setDescricao("A".repeat(101));
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("100 caracteres"));
    }

    @Test
    void deveLancarErroQuandoCargaHorariaMenorQue1NaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setCargaHoraria(0L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("1 e 300"));
    }

    @Test
    void deveLancarErroQuandoCargaHorariaMaiorQue300NaAtualizacao() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("CURSO123");
        dto.setCargaHoraria(301L);
        ErroDeRequisicaoGeral e = assertThrows(ErroDeRequisicaoGeral.class, () -> validator.validarAtualizacao(dto));
        assertTrue(e.getMessage().contains("1 e 300"));
    }
}