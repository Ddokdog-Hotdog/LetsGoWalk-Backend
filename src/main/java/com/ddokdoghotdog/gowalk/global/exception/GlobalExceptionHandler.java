package com.ddokdoghotdog.gowalk.global.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusniessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handle(AccessDeniedException e) {
        final ErrorCode errorCode = ErrorCode.NO_PERMISSION;

        log.error("AccessDeniedException", errorCode);
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        log.error("IllegalArgumentException", errorCode);
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        final ErrorCode errorCode = ErrorCode.ENTITY_NOT_FOUND;

        log.error("EntityNotFoundException", errorCode);
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException e) {
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("JsonProcessingException", e);
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(UncategorizedMongoDbException.class)
    protected ResponseEntity<ErrorResponse> handleMongoDBException(UncategorizedMongoDbException e) {
        final ErrorCode errorCode = ErrorCode.MONGO_QUERY_EXECUTION_ERROR;
        log.error("MongoDBException", e);
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    // 최종 에러처리
    // @ExceptionHandler(Exception.class)
    // protected ResponseEntity<ErrorResponse> handle(Exception e) {
    // log.error("INTERNAL_SERVER_ERROR", ErrorCode.INTERNAL_SERVER_ERROR);
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }

}
