package com.example.calculator.utils;

public class IncorrectOperationException extends RuntimeException {
    public IncorrectOperationException(String message) {
        super(message);
    }
}
