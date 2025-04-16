package com.example.da1_backend.exception;

public class RouteException extends RuntimeException {
    public RouteException(String message) {
        super(message);
    }

    public static final String ROUTE_NOT_FOUND = "Route not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROUTE_NOT_FOUND_WITH_ID = "Route not found with ID: ";
    public static final String USER_HAS_ROUTE_IN_PROGRESS   = "User has route in progress";

}
