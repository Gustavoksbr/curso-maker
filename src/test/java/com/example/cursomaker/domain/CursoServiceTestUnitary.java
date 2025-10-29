package com.example.cursomaker.domain;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.repository.CursoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
        // Por ora, não se vê a necessidade de muitos testes unitários aqui, pois a maior parte da lógica já está testada no Validator e no Repository

class CursoServiceTestUnitary {
    @MockitoBean
    private CursoRepository cursoRepository;
    @MockitoBean
    private CursoValidator cursoValidator;
    @Autowired
    private CursoService cursoService;
    @Test
    void deveDelegarFindAllParaRepository() {
        when(cursoRepository.findAll()).thenReturn(List.of(new Curso("C1","Titulo","Desc",10L)));

        cursoService.findAll();

        verify(cursoRepository).findAll();
    }

    @Test
    void deveDelegarFindByParametrosParaRepository() {
        when(cursoRepository.findByParametros("", "", 0L, Long.MAX_VALUE))
                .thenReturn(List.of(new Curso("C1","Titulo","Desc",10L)));

        cursoService.findByParametros(null, null, null, null);

        verify(cursoRepository).findByParametros("", "", 0L, Long.MAX_VALUE);
    }

    @Test
    void deveCriarCursoChamandoValidatorERepository() {
        Curso curso = new Curso("C1","Titulo","Desc",10L);
        when(cursoRepository.create(curso)).thenReturn(curso);

        cursoService.create(curso);

        verify(cursoValidator).validarCriacao(curso);
        verify(cursoRepository).create(curso);
    }

    @Test
    void deveAtualizarCursoChamandoValidatorERepository() {
        CursoParaAtualizar dto = new CursoParaAtualizar();
        dto.setCodigo("C1");
        dto.setTitulo("NovoTitulo");

        Curso cursoAtualizado = new Curso("C1","NovoTitulo","Desc",10L);
        when(cursoRepository.update(dto)).thenReturn(cursoAtualizado);

        cursoService.update(dto);

        verify(cursoValidator).validarAtualizacao(dto);
        verify(cursoRepository).update(dto);
    }

    @Test
    void deveDeletarCursoChamandoRepository() {
        String codigo = "C1";
        doNothing().when(cursoRepository).delete(codigo);

        cursoService.delete(codigo);
        verify(cursoValidator).validarDelecao(codigo);
        verify(cursoRepository).delete(codigo);
    }

}