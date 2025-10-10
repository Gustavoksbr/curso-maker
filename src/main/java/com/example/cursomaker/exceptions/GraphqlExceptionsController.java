package com.example.cursomaker.exceptions;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.execution.ErrorType;

@ControllerAdvice
public class GraphqlExceptionsController {

    @GraphQlExceptionHandler(NenhumCursoEncontrado.class)
    public GraphQLError handleNenhumCursoEncontrado(NenhumCursoEncontrado ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .build();
    }
    // 400
    @GraphQlExceptionHandler(MethodArgumentNotValidException.class)
    public GraphQLError handleValidationExceptions(MethodArgumentNotValidException ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }

    @GraphQlExceptionHandler(ErroDeRequisicaoGeral.class)
    public GraphQLError handleBadRequest(ErroDeRequisicaoGeral ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }
    // 404
    @GraphQlExceptionHandler(CursoNaoEncontrado.class)
    public GraphQLError handleNotFound(CursoNaoEncontrado ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .build();
    }

    // 409
    @GraphQlExceptionHandler(Erro409.class)
    public GraphQLError handleConflict(Erro409 ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(CustomErrorType.CONFLICT)
                .build();
    }

    public enum CustomErrorType implements graphql.ErrorClassification {
        CONFLICT
    }

}
