package ru.practicum.ewmmain.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmmain.controller.privats.RequestPrivateController;
import ru.practicum.ewmmain.exception.BadDataRequestException;
import ru.practicum.ewmmain.exception.BadRequestEventException;
import ru.practicum.ewmmain.model.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = {RequestPrivateController.class})
public class RequestErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleCompilationConstraintException(final BadDataRequestException e) {
        return ApiError.builder()
                .errors("BadDataRequestException")
                .message(e.getMessage())
                .reason("Bad data for request")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCompilationConstraintException(final MissingServletRequestParameterException e) {
        return ApiError.builder()
                .errors("MissingServletRequestParameterException")
                .message(e.getMessage())
                .reason("Bad data for request")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCompilationConstraintException(final BadRequestEventException e) {
        return ApiError.builder()
                .errors("BadRepeatRequestException")
                .message(e.getMessage())
                .reason("Request conflict")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
