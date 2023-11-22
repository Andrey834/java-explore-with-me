package ru.practicum.ewmmain.exception;

public class BadParamForEventSearchException extends RuntimeException {
    public BadParamForEventSearchException(String message) {
        super(message);
    }
}