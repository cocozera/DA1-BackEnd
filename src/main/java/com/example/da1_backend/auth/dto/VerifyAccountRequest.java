package com.example.da1_backend.auth.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyAccountRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String code;
}

