package com.r2s.structure_sample.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ResponseObject<T> {
    private String message;
    private T data;
    private HttpStatus status;
}
