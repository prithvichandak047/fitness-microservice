package com.fitness.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class RegisterRequest {
    @NotBlank(message = "email cannot be empty")
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "password cannot be empty")
    @Size(min = 6, message = "minnimum 6 digits are required")
    private String password;
    private String firstName;
    private String lastName;
}
