package ru.practicum.ewmmain.exception;

public class BadDataRequestException extends RuntimeException {
    public BadDataRequestException(String message) {
        super(message);
    }
}