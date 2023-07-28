package com.example.demo.service.exception;

public class UserNotFoundException extends Exception {

    private static final String USER_NOT_FOUND_EXCEPTION_TEXT = "User with username = %s not found.";

    public UserNotFoundException(String username) {
        super(String.format(USER_NOT_FOUND_EXCEPTION_TEXT, username));
    }
}
