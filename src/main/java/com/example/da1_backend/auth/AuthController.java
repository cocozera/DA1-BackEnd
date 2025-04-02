package com.example.da1_backend.auth;

import com.example.da1_backend.auth.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyAccount(@Valid @RequestParam VerifyAccountRequest request) {
        return ResponseEntity.ok(authService.verifyAccount(request));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<AuthResponse> recoverPassword(@Valid @RequestParam RecoverPasswordRequest request) {
        AuthResponse response = authService.recoverPassword(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePasswordWithCode(@Valid @RequestParam ChangePasswordRequest request) {
        AuthResponse response = authService.changePasswordWithCode(request);
        return ResponseEntity.ok(response);
    }
}

