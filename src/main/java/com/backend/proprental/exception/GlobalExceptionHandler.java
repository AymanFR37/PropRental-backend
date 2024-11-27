package com.backend.proprental.exception;

import com.backend.proprental.payload.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorMessage.builder()
                        .statusCode(BAD_REQUEST.value())
                        .description("Error: " + ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder()
                        .statusCode(INTERNAL_SERVER_ERROR.value())
                        .description("An unexpected error occurred: " + ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND)
                .body(ErrorMessage.builder()
                        .statusCode(NOT_FOUND.value())
                        .description("An unexpected error occurred: " + ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationCredentialsNotFoundException ex) {
        return  ResponseEntity.status(UNAUTHORIZED)
                .body(ErrorMessage.builder()
                        .statusCode(UNAUTHORIZED.value())
                        .description("Authentication credentials are missing or invalid: " + ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
