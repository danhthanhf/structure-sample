package com.r2s.structure_sample.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class RegisterRequest{
    @NotBlank(message = "Email is not blank!")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is not blank!")
    @Size(min = 6, max =  100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "First name is not blank!")
    @Size(min = 2, message = "First name must be at least 2 characters")
    private String firstName;

    @NotBlank(message = "Last name is not blank!")
    @Size(min = 2, message = "Last name must be at least 2 characters")
    private String lastName;
}
