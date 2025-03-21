package com.example.da1_backend.auth;

import com.example.da1_backend.auth.dto.AuthResponse;
import com.example.da1_backend.auth.dto.LoginRequest;
import com.example.da1_backend.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyAccount(@RequestParam String email, @RequestParam String code) {
        return ResponseEntity.ok(authService.verifyAccount(email, code));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<AuthResponse> recoverPassword(@RequestParam String email) {
        AuthResponse response = authService.recoverPassword(email);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePasswordWithCode(@RequestParam String email,
                                                               @RequestParam String code,
                                                               @RequestParam String newPassword) {
        AuthResponse response = authService.changePasswordWithCode(email, code, newPassword);
        return ResponseEntity.ok(response);
    }
}

