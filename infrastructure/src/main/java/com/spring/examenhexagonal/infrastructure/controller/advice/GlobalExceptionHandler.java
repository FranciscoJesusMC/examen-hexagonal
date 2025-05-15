package com.spring.examenhexagonal.infrastructure.controller.advice;

import com.spring.examenhexagonal.domain.aggregates.dto.ResponseBase;
import com.spring.examenhexagonal.infrastructure.exception.ConsultaSunatException;
import com.spring.examenhexagonal.infrastructure.exception.DuplicateResourceException;
import com.spring.examenhexagonal.infrastructure.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseBase<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseBase<?> responseBase = new ResponseBase<>(ex.getMessage(),404, Optional.empty());
        return new ResponseEntity<>(responseBase, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBase<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });
        ResponseBase<?> responseBase = new ResponseBase<>("Los datos enviados no son validos",400, Optional.of(errors));
        return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConsultaSunatException.class)
    public ResponseEntity<ResponseBase<?>> handleConsultaSunatException(ConsultaSunatException ex) {
        ResponseBase<?> responseBase = new ResponseBase<>(ex.getMessage(),500, Optional.empty());
        return new ResponseEntity<>(responseBase, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ResponseBase<?>> handleDuplicateResourceException(DuplicateResourceException ex) {
        ResponseBase<?> responseBase = new ResponseBase<>(ex.getMessage(),409, Optional.empty());
        return new ResponseEntity<>(responseBase, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBase<?>> handleException(Exception ex) {
        ResponseBase<?> responseBase = new ResponseBase<>(ex.getMessage(),500, Optional.empty());
        return new ResponseEntity<>(responseBase, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
