package com.example.da1_backend.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public static final String USER_NOT_FOUND_WITH_ID = "Usuario no encontrado con id: ";
}
