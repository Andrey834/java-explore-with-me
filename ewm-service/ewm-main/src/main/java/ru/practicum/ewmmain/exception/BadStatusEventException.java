package ru.practicum.ewmmain.exception;

public class BadStatusEventException extends RuntimeException {
    public BadStatusEventException(String message) {
        super(message);
    }
}