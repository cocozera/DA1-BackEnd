package com.example.da1_backend.exception;

public class PackageException extends RuntimeException {
    public PackageException(String message) {
        super(message);
    }

    public static final String PACKAGE_NOT_FOUND = "Package not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String QR_GENERATION_FAILED = "Failed to generate QR code";
}
