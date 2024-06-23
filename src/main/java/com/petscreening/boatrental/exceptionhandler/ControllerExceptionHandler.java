package com.petscreening.boatrental.exceptionhandler;

import graphql.GraphQLError;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleValidationException(ValidationException exception) {
        log.warn("Validation error", exception);
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(exception.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleBindException(BindException exception) {
        log.warn("Validation error", exception);
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(exception.getMessage()).build();
    }
}
