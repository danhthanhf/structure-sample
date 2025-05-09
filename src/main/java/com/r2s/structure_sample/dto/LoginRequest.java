package com.r2s.structure_sample.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email is not blank!")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is not blank!")
    @Size(min = 6, max =  100, message = "Password must be between 6 and 100 characters")
    private String password;
}
