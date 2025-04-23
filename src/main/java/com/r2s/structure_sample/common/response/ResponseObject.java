package com.r2s.structure_sample.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ResponseObject {
    private String message;
    private Object data;
    private HttpStatus status;
}
