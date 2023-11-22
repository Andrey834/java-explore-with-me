package ru.practicum.ewmmain.exception;

public class BadDataEventException extends RuntimeException {
    public BadDataEventException(String message) {
        super(message);
    }
}