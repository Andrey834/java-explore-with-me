package ru.practicum.ewmmain.controller.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmmain.controller.admin.UsersAdminController;
import ru.practicum.ewmmain.controller.privats.RequestPrivateController;
import ru.practicum.ewmmain.model.ApiError;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {UsersAdminController.class, RequestPrivateController.class})
public class UserErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final NoSuchElementException e) {
        return ApiError.builder()
                .errors("NoSuchElementException")
                .message(e.getMessage())
                .reason("User not found")
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUserConstraintException(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .errors("MethodArgumentNotValidException")
                .message(e.getMessage())
                .reason("Bad data for register user")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserConstraintException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .errors("ConstraintViolationException")
                .message(e.getMessage())
                .reason("Name is already taken")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
