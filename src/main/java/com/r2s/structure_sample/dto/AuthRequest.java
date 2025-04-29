package com.r2s.structure_sample.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class AuthRequest {
    @NotEmpty(message = "Email is not empty!")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is not empty!")
    @Size(min = 6, max =  100, message = "Password must be between 6 and 100 characters")
    private String password;

    private String firstName;
    private String lastName;
}
