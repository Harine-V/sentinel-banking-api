package com.sentinel.banking_api.exception;

import java.time.LocalDateTime;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import com.sentinel.banking_api.model.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
             ex.getMessage(), 
             request.getDescription(false)
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleAccountNotFound(AccountNotFoundException ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(
            LocalDateTime.now(),
             ex.getMessage(), 
             request.getDescription(false)
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage=ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorDetails error =new ErrorDetails(
            LocalDateTime.now(), 
            "Validation Failed: "+errorMessage,
             "Check your input data"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}