package com.r2s.structure_sample.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String lastName;
    private String firstName;
    private String accessToken;
}
