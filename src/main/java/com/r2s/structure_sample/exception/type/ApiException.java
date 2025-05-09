package com.r2s.structure_sample.exception.type;

public abstract class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
