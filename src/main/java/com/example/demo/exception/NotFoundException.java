package com.example.demo.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String localizedMessage) {
        super(localizedMessage);
    }
}

