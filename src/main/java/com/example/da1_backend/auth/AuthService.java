package com.example.da1_backend.auth;

import com.example.da1_backend.auth.dto.*;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import com.example.da1_backend.util.JwtUtil;
import com.example.da1_backend.email.EmailService;
import com.example.da1_backend.exception.AuthException;
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
            throw new AuthException(AuthException.EMAIL_ALREADY_TAKEN);
        }

        String verificationCode = generateVerificationCode();

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEnabled(false);
        user.setVerificationCode(verificationCode);

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationCode);

        return new AuthResponse(null, user.getId(), "User registered successfully. Check your email for verification code.");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthException.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(AuthException.INVALID_CREDENTIALS);
        }

        if (!user.isEnabled()) {
            throw new AuthException(AuthException.ACCOUNT_NOT_VERIFIED);
        }

        String token = jwtUtil.generateAccessToken(user);
        return new AuthResponse(token, user.getId(), "Login successful");
    }

    public AuthResponse verifyAccount(VerifyAccountRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthException.USER_NOT_FOUND));

        if (user.isEnabled()) {
            return new AuthResponse(null, user.getId(), "Account already verified.");
        }

        if (!user.getVerificationCode().equals(request.getCode())) {
            throw new AuthException(AuthException.INVALID_VERIFICATION_CODE);
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);

        return new AuthResponse(null, user.getId(), "Account verified successfully.");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public AuthResponse recoverPassword(RecoverPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthException.USER_NOT_FOUND));

        String resetCode = generateVerificationCode();
        user.setVerificationCode(resetCode);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), resetCode);

        return new AuthResponse(null, user.getId(), "A password reset code has been sent to your email.");
    }

    public AuthResponse changePasswordWithCode(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new AuthException(AuthException.USER_NOT_FOUND));

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(changePasswordRequest.getCode())) {
            throw new AuthException(AuthException.INVALID_VERIFICATION_CODE);
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setVerificationCode(null);
        userRepository.save(user);

        return new AuthResponse(null, user.getId(), "Password changed successfully.");
    }

    public void validateToken(String token) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new AuthException(AuthException.TOKEN_EXPIRED);
            }
        } catch (Exception e) {
            throw new AuthException(AuthException.TOKEN_INVALID);
        }
    }

}
