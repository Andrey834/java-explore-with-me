package ru.practicum.ewmmain.exception;

public class BadDateEventException extends RuntimeException {
    public BadDateEventException(String message) {
        super(message);
    }
}