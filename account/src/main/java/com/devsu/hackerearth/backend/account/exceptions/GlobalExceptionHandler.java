package com.devsu.hackerearth.backend.account.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            new Date(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handeDuplicate(DuplicateResourceException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
            new Date(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(f->f.getField() + ": "+ f.getDefaultMessage())
                .reduce((a,b)-> a+ "; " +b)
                .orElse("Datos invalidos");

        ErrorResponse error = new ErrorResponse(
            new Date(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            message,
            request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)  
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            new Date(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientBalanceException.class)  
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(Exception ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            new Date(),
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<ErrorResponse> handleAccountInactive(AccountInactiveException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            new Date(),
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
