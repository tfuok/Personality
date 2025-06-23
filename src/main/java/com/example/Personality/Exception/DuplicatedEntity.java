package com.example.Personality.Exception;

public class DuplicatedEntity extends RuntimeException {
    public DuplicatedEntity(String message) {
        super(message);
    }
}
