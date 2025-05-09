package com.r2s.structure_sample.exception;

import com.r2s.structure_sample.common.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> responseObjectResponseEntity(EntityNotFoundException e) {
        var response = ApiResponse.failure(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceConflict(ResourceConflictException e) {
        var response = ApiResponse.failure(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // validate request body DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println(ex.getBindingResult());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        var res = ApiResponse.failure("Validation error", errors);
        return ResponseEntity.unprocessableEntity().body(res);
    }

    // validate request param
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        System.out.println(ex.getConstraintViolations());

        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        var res = ApiResponse.failure(ex.getMessage(), errors);
        return ResponseEntity.unprocessableEntity().body(res);
    }
}
