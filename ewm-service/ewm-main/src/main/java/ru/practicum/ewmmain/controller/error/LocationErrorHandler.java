package ru.practicum.ewmmain.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmmain.controller.admin.LocationAdminController;
import ru.practicum.ewmmain.exception.DuplicateLocationException;
import ru.practicum.ewmmain.model.ApiError;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {LocationAdminController.class})
public class LocationErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCompilationConstraintException(final DuplicateLocationException e) {
        return ApiError.builder()
                .errors("DuplicateLocationException")
                .message(e.getMessage())
                .reason("Duplicate location in system")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationConstraintException(final NoSuchElementException e) {
        return ApiError.builder()
                .errors("NoSuchElementException")
                .message(e.getMessage())
                .reason("Location was not found")
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
