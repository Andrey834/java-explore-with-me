package ru.practicum.ewmmain.exception;

public class BadRequestEventException extends RuntimeException {
    public BadRequestEventException(String message) {
        super(message);
    }
}