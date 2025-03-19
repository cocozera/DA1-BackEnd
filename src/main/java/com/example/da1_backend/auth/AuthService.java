package com.example.da1_backend.auth;

import com.example.da1_backend.auth.dto.RegisterRequest;
import com.example.da1_backend.util.JwtUtil;
import com.example.da1_backend.auth.dto.AuthResponse;
import com.example.da1_backend.auth.dto.LoginRequest;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service  // ✅ Asegúrate de que esta anotación esté presente
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }


        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEnabled(false);

        userRepository.save(user);
        return new AuthResponse(null, "User registered successfully");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateAccessToken(user);
        return new AuthResponse(token, "Login successful");
    }
}
