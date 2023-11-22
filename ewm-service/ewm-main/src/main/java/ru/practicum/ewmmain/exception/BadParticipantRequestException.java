package ru.practicum.ewmmain.exception;

public class BadParticipantRequestException extends RuntimeException {
    public BadParticipantRequestException(String message) {
        super(message);
    }
}