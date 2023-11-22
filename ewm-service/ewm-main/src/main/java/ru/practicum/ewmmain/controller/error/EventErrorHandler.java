package ru.practicum.ewmmain.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmmain.controller.admin.EventAdminController;
import ru.practicum.ewmmain.controller.privats.EventPrivateController;
import ru.practicum.ewmmain.controller.privats.RequestPrivateController;
import ru.practicum.ewmmain.controller.publics.EventPublicController;
import ru.practicum.ewmmain.exception.BadDataEventException;
import ru.practicum.ewmmain.exception.BadDateEventException;
import ru.practicum.ewmmain.exception.BadParamForEventSearchException;
import ru.practicum.ewmmain.exception.BadParticipantRequestException;
import ru.practicum.ewmmain.exception.BadStatusEventException;
import ru.practicum.ewmmain.model.ApiError;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {EventAdminController.class, RequestPrivateController.class,
        EventPrivateController.class, EventPublicController.class})
public class EventErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleCompilationConstraintException(final BadDataEventException e) {
        return ApiError.builder()
                .errors("BadDataEventException")
                .message(e.getMessage())
                .reason("Bad data for update event")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(final NoSuchElementException e) {
        return ApiError.builder()
                .errors("NoSuchElementException")
                .message(e.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCompilationNotFoundException(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .errors("MethodArgumentNotValidException")
                .message(e.getMessage())
                .reason("Argument not Valid")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCompilationNotFoundException(final BadParamForEventSearchException e) {
        return ApiError.builder()
                .errors("BadParamForEventSearchException")
                .message(e.getMessage())
                .reason("Param not Valid")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUpdateDateEventException(final BadDateEventException e) {
        return ApiError.builder()
                .errors("BadDateEventException")
                .message(e.getMessage())
                .reason("Date not Valid")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUpdateDateEventException(final BadStatusEventException e) {
        return ApiError.builder()
                .errors("BadStatusEventException")
                .message(e.getMessage())
                .reason("Event has already been published")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUpdateDateEventException(final BadParticipantRequestException e) {
        return ApiError.builder()
                .errors("BadParticipantRequestException")
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
