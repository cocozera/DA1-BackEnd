package com.example.da1_backend.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotNull @NotEmpty
    private String name;

    @Email(message = "Invalid email format")
    @NotNull @NotEmpty
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull @NotEmpty
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    @NotNull @NotEmpty
    private String phoneNumber;
}
