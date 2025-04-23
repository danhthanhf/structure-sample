package com.r2s.structure_sample.exception;

import com.r2s.structure_sample.common.response.ResponseObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseObject> responseObjectResponseEntity(EntityNotFoundException e) {
        var response = ResponseObject.builder().status(HttpStatus.NOT_FOUND).message(e.getMessage()).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ResponseObject> handleResourceConflict(ResourceConflictException e) {
        var response = ResponseObject.builder().status(HttpStatus.CONFLICT).message(e.getMessage()).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();
        var res = ResponseObject.builder()
                .status(HttpStatus.BAD_REQUEST).message("error").data(errors).build();
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
