package com.example.da1_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoverPasswordRequest {
    @NotBlank
    private String email;
}
