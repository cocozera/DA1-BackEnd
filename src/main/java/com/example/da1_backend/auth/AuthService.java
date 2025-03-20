package com.example.da1_backend.auth;

import com.example.da1_backend.auth.dto.RegisterRequest;
import com.example.da1_backend.auth.dto.AuthResponse;
import com.example.da1_backend.auth.dto.LoginRequest;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import com.example.da1_backend.util.JwtUtil;
import com.example.da1_backend.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        // Generar código de verificación aleatorio
        String verificationCode = generateVerificationCode();

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEnabled(false);
        user.setVerificationCode(verificationCode);

        userRepository.save(user);

        // Enviar email con el código de verificación
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);

        return new AuthResponse(null, "User registered successfully. Check your email for verification code.");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is not verified. Check your email.");
        }

        String token = jwtUtil.generateAccessToken(user);
        return new AuthResponse(token, "Login successful");
    }

    public AuthResponse verifyAccount(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            return new AuthResponse(null, "Account already verified.");
        }

        if (!user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Invalid verification code.");
        }

        user.setEnabled(true);
        user.setVerificationCode(null); // Eliminar el código después de activación
        userRepository.save(user);

        return new AuthResponse(null, "Account verified successfully.");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // Código de 6 dígitos
    }

    public AuthResponse recoverPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        String resetCode = generateVerificationCode();
        user.setVerificationCode(resetCode);
        userRepository.save(user);


        emailService.sendVerificationEmail(user.getEmail(), resetCode);

        return new AuthResponse(null, "A password reset code has been sent to your email.");
    }

    public AuthResponse changePasswordWithCode(String email, String code, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Invalid verification code.");
        }


        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        userRepository.save(user);

        return new AuthResponse(null, "Password changed successfully.");
    }



}
