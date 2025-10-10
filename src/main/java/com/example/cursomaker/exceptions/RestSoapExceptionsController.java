package com.example.cursomaker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice // serve para rest e soap

// O REST sempre retorna os status correspondentes, ja o SOAP sempre retorna 500

public class RestSoapExceptionsController {

    // 400
    @ExceptionHandler(ErroDeRequisicaoGeral.class)
    public ResponseEntity<String> handleErroDeRequisicaoGeral(ErroDeRequisicaoGeral ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // provavelmente erro no corpo da requisição com spring validation
    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder erros = new StringBuilder();

        // Extrai os erros de campo e os adiciona à string
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            if(errorMessage!=null && errorMessage.equals("deve corresponder a \"^[a-zA-Z0-9]+$\"")) {
                errorMessage = "deve conter apenas letras e números.";
            }
            erros.append("Campo '").append(fieldName).append("': ").append(errorMessage).append(". ");
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros.toString().trim());
    }

    // erro explícito no corpo da requisição
    //400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Corpo da requisição inválido: "+ex.getMessage());
    }


    // ====================================================================================================
    //404
    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<String> NoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro 404: URL não encontrada.");
    }
    //404
    @ExceptionHandler(NenhumCursoEncontrado.class)
    public ResponseEntity<String> handleNenhumCursoEncontrado(NenhumCursoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    //404
    @ExceptionHandler(CursoNaoEncontrado.class)
    public ResponseEntity<String> NotFoundException(CursoNaoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // ====================================================================================================

    //405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Método HTTP não permitido para esta URL.");
    }

    // 409

    @ExceptionHandler(Erro409.class)
    public ResponseEntity<String> handleErro409(Erro409 ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    // ====================================================================================================
    // 500
    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception ex) {
        System.out.println("Erro 500: "+ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
    }

}