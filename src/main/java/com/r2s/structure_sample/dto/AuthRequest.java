package com.r2s.structure_sample.dto;


import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
