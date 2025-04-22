package com.example.da1_backend.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    // Mensajes comunes como constantes (opcional)
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_TAKEN = "Email is already taken";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ACCOUNT_NOT_VERIFIED = "Account is not verified. Check your email.";
    public static final String INVALID_VERIFICATION_CODE = "Invalid verification code.";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";

}