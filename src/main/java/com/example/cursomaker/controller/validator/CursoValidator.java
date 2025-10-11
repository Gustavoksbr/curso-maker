package com.example.cursomaker.controller.validator;

import com.example.cursomaker.dominio.Curso;
import com.example.cursomaker.exceptions.ErroDeRequisicaoGeral;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CursoValidator {

    private static final Pattern CODIGO_PATTERN = Pattern.compile("^[A-Za-zÀ-ÿ0-9 ]+$");

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

    public void validarAtualizacao(Curso dto, String codigoNovo) {

        if (!isBlank(codigoNovo)) {
            if (codigoNovo.length() > 15) {
                throw new ErroDeRequisicaoGeral("Campo 'codigoNovo': deve ter no máximo 15 caracteres.");
            }
            if (!CODIGO_PATTERN.matcher(codigoNovo).matches()) {
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

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}