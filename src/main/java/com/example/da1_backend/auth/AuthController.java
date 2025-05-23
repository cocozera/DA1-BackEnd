package com.example.da1_backend.auth;

import com.example.da1_backend.auth.dto.*;
import com.example.da1_backend.exception.AuthException;
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
    public ResponseEntity<AuthResponse> verifyAccount(@Valid @RequestBody VerifyAccountRequest request) {
        return ResponseEntity.ok(authService.verifyAccount(request));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<AuthResponse> recoverPassword(@Valid @RequestBody RecoverPasswordRequest request) {
        AuthResponse response = authService.recoverPassword(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePasswordWithCode(@Valid @RequestBody ChangePasswordRequest request) {
        AuthResponse response = authService.changePasswordWithCode(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException(AuthException.TOKEN_INVALID);
        }

        String token = authHeader.substring(7);
        authService.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
}

