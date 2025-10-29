package com.example.cursomaker.domain;

import com.example.cursomaker.domain.model.Curso;
import com.example.cursomaker.domain.model.CursoParaAtualizar;
import com.example.cursomaker.exceptions.ErroDeRequisicaoGeral;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class CursoValidator {

    private static final Pattern CODIGO_PATTERN = Pattern.compile("^[A-Za-zÀ-ÿ0-9 ]+$");
    private static final List<String> CODIGOS_CURSOS_APENAS_LEITURA = new ArrayList<>(List.of("c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10"));

    public void validarCriacao(Curso dto) {

        // ---- Código ----
        if (isBlank(dto.getCodigo())) {
            throw new ErroDeRequisicaoGeral("Campo 'codigo': não deve estar em branco.");
        }
        if (dto.getCodigo().length() > 15) {

            throw new ErroDeRequisicaoGeral("Campo 'codigo': deve ter no máximo 15 caracteres.");
        }
        if (!CODIGO_PATTERN.matcher(dto.getCodigo()).matches()) {
            throw new ErroDeRequisicaoGeral("Campo 'codigo': deve conter apenas letras e números.");
        }

        // ---- Título ----
        if (isBlank(dto.getTitulo())) {
            throw new ErroDeRequisicaoGeral("Campo 'titulo': não deve estar em branco.");
        }
        if (dto.getTitulo() != null && dto.getTitulo().length() > 30) {
            throw new ErroDeRequisicaoGeral("Campo 'titulo': deve ter no máximo 30 caracteres.");
        }

        // ---- Descrição ----
        if(isBlank(dto.getDescricao())) {
            throw new ErroDeRequisicaoGeral("Campo 'descricao': não deve estar em branco.");
        }
        if (dto.getDescricao() != null && dto.getDescricao().length() > 100) {
            throw new ErroDeRequisicaoGeral("Campo 'descricao': deve ter no máximo 100 caracteres.");
        }

        // ---- Carga Horária ----
        if (dto.getCargaHoraria() == null) {
            throw new ErroDeRequisicaoGeral("Campo 'cargaHoraria': não deve ser nulo.");
        }
        if (dto.getCargaHoraria() < 1 || dto.getCargaHoraria() > 300) {
            throw new ErroDeRequisicaoGeral("Campo 'cargaHoraria': deve estar entre 1 e 300.");
        }
    }

    public void validarAtualizacao(CursoParaAtualizar dto) {

        if (isBlank(dto.getCodigo())) {
            throw new ErroDeRequisicaoGeral("Escolha o código do curso que deseja atualizar.");
        }else{
            if(CODIGOS_CURSOS_APENAS_LEITURA.contains(dto.getCodigo())){
                throw new ErroDeRequisicaoGeral("O curso de código '" + dto.getCodigo() + "' é somente leitura e não pode ser atualizado. Os cursos somente leitura são: " + CODIGOS_CURSOS_APENAS_LEITURA);
            }
        }

        if (!isBlank(dto.getCodigoNovo())) {
            if (dto.getCodigoNovo().length() > 15) {
                throw new ErroDeRequisicaoGeral("Campo 'codigoNovo': deve ter no máximo 15 caracteres.");
            }
            if (!CODIGO_PATTERN.matcher(dto.getCodigoNovo()).matches()) {
                throw new ErroDeRequisicaoGeral("Campo 'codigoNovo': deve conter apenas letras e números.");
            }
        }

        // ---- Título ----
        if (dto.getTitulo() != null && dto.getTitulo().length() > 30) {
            throw new ErroDeRequisicaoGeral("Campo 'titulo': deve ter no máximo 30 caracteres.");
        }

        // ---- Descrição ----
        if (dto.getDescricao() != null && dto.getDescricao().length() > 100) {
            throw new ErroDeRequisicaoGeral("Campo 'descricao': deve ter no máximo 100 caracteres.");
        }

        // ---- Carga Horária ----
        if (dto.getCargaHoraria() != null) {
            if (dto.getCargaHoraria() < 1 || dto.getCargaHoraria() > 300) {
                throw new ErroDeRequisicaoGeral("Campo 'cargaHoraria': deve estar entre 1 e 300.");
            }
        }
    }

    public void validarDelecao(String codigo) {
            if(CODIGOS_CURSOS_APENAS_LEITURA.contains(codigo)){
                throw new ErroDeRequisicaoGeral("O curso de código '" + codigo + "' é somente leitura e não pode ser deletado. Os cursos somente leitura são: " + CODIGOS_CURSOS_APENAS_LEITURA);
            }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}